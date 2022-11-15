package com.tencentcloudapi.cls.producer.common;

import com.tencentcloudapi.cls.producer.AsyncProducerConfig;
import com.tencentcloudapi.cls.producer.Result;
import com.tencentcloudapi.cls.producer.errors.LogSizeTooLargeException;
import com.tencentcloudapi.cls.producer.http.client.Sender;
import com.tencentcloudapi.cls.producer.http.comm.HttpMethod;
import com.tencentcloudapi.cls.producer.http.comm.RequestMessage;
import com.tencentcloudapi.cls.producer.request.PutLogsRequest;
import com.tencentcloudapi.cls.producer.response.PutLogsResponse;
import com.tencentcloudapi.cls.producer.util.LZ4Encoder;
import com.tencentcloudapi.cls.producer.util.QcloudClsSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SendProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendProducerBatchTask.class);

    private final AsyncProducerConfig producerConfig;

    public SendProducer(AsyncProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
    }

    private Map<String, String> getCommonHeadPara() {
        HashMap<String, String> headParameter = new HashMap<>(3);
        headParameter.put(Constants.CONST_CONTENT_LENGTH, "0");
        headParameter.put(Constants.CONST_CONTENT_TYPE, Constants.CONST_PROTO_BUF);
        headParameter.put(Constants.CONST_HOST, producerConfig.getHostName());
        return headParameter;
    }

    /**
     * buildPutLogsRequest
     * @param logItems
     * @param topicID
     * @return
     */
    private PutLogsRequest buildPutLogsRequest(List<LogItem> logItems, String topicID) {
        Logs.LogGroup.Builder logGroup = Logs.LogGroup.newBuilder();
        for(LogItem tmp:logItems){
            logGroup.addLogs(tmp.mContents);
        }
        return new PutLogsRequest(topicID, producerConfig.getSourceIp(), "", logGroup);
    }


    /**
     * 构造报文，发送日志
     *
     * @param urlParameter  url param
     * @param headParameter headers
     * @param body          cls pb serialize body
     * @throws LogException  LogException
     */
    private PutLogsResponse sendLogs(Map<String, String> urlParameter, Map<String, String> headParameter, byte[] body) throws LogException {
        headParameter.put(Constants.CONST_CONTENT_LENGTH, String.valueOf(body.length));
        String signature;
        try {
            signature = QcloudClsSignature.buildSignature(producerConfig.getSecretId(), producerConfig.getSecretKey(), HttpMethod.POST.toString(), Constants.UPLOAD_LOG_RESOURCE_URI, urlParameter, headParameter, 300000);
        } catch (UnsupportedEncodingException e) {
            throw new LogException(ErrorCodes.ENCODING_EXCEPTION, e.getMessage());
        }
        headParameter.put(Constants.CONST_AUTHORIZATION, signature);
        headParameter.put("x-cls-compress-type", "lz4");

        if (null != producerConfig.getSecretToken() && !producerConfig.getSecretToken().isEmpty()) {
            headParameter.put("X-Cls-Token", producerConfig.getSecretToken());
        }
        headParameter.put("cls-java-sdk-version", "1.0.13");

        URI uri = getHostURI();
        byte[] compressedData = LZ4Encoder.compressToLhLz4Chunk(body);
        RequestMessage requestMessage = buildRequest(uri, urlParameter, headParameter, compressedData, compressedData.length);
        PutLogsResponse response;
        String requestId = "";
        try {
            response = Sender.doPost(requestMessage);
            if (response !=null) {
                requestId = response.GetRequestId();
            }
        } catch (Exception e) {
            throw new LogException(ErrorCodes.SendFailed, e.getMessage());
        }
        switch (response.GetHttpStatusCode()) {
            case 200: return response;
            case 500: throw new LogException(response.GetHttpStatusCode(), ErrorCodes.BAD_RESPONSE, "internal server error", requestId);
            case 429: throw new LogException(response.GetHttpStatusCode(), ErrorCodes.SpeedQuotaExceed, "speed quota exceed", requestId);
            case 413: throw new LogException(response.GetHttpStatusCode(), ErrorCodes.ContentIsTooLarge, "content is too large", requestId);
            case 404: throw new LogException(response.GetHttpStatusCode(), ErrorCodes.TopicNotExists, "topic not exists", requestId);
            case 403: throw new LogException(response.GetHttpStatusCode(), ErrorCodes.SingleValueExceed1M, "single log value exceed 1M", requestId);
            case 401: throw new LogException(response.GetHttpStatusCode(), ErrorCodes.AuthFailure, "auth failed", requestId);
            case 400: throw new LogException(response.GetHttpStatusCode(), ErrorCodes.InvalidParam, "invalid param", requestId);
            default: throw new LogException(response.GetHttpStatusCode(), ErrorCodes.BAD_RESPONSE, response.GetAllHeaders().toString(), requestId);
        }
    }

    /**
     * 获取host uri
     * @return URI
     */
    private URI getHostURI() {
        String endPointUrl = producerConfig.getHttpType() + producerConfig.getHostName();
        try {
            return new URI(endPointUrl);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(ErrorCodes.ENDPOINT_INVALID, e);
        }
    }

    private static RequestMessage buildRequest(URI endpoint,
                                               Map<String, String> parameters,
                                               Map<String, String> headers,
                                               byte[] content,
                                               long size)
    {
        RequestMessage request = new RequestMessage();
        request.setMethod(HttpMethod.POST);
        request.setEndpoint(endpoint);
        request.setResourcePath(Constants.UPLOAD_LOG_RESOURCE_URI);
        request.setParameters(parameters);
        request.setHeaders(headers);
        request.setContent(content);
        request.setContentLength(size);
        return request;
    }

    private Attempt buildAttempt(Exception e, long nowMs, String requestId) {
        if (e instanceof LogException) {
            LogException logException = (LogException) e;
            return new Attempt(
                    false,
                    logException.GetRequestId(),
                    logException.GetErrorCode(),
                    logException.GetErrorMessage(),
                    nowMs);
        } else {
            return new Attempt(false, "", ErrorCodes.BAD_RESPONSE, e.getMessage(), nowMs);
        }
    }

    private void ensureValidLogSize(int sizeInBytes) throws LogSizeTooLargeException {
        if (sizeInBytes > Constants.MAX_BATCH_SIZE_IN_BYTES) {
            throw new LogSizeTooLargeException(
                    "the logs is "
                            + sizeInBytes
                            + " bytes which is larger than MAX_BATCH_SIZE_IN_BYTES "
                            + Constants.MAX_BATCH_SIZE_IN_BYTES);
        }
        if (sizeInBytes > producerConfig.getTotalSizeInBytes()) {
            throw new LogSizeTooLargeException(
                    "the logs is "
                            + sizeInBytes
                            + " bytes which is larger than the totalSizeInBytes you specified");
        }
    }

    public Result sendProducer(long nowMs, String topicID, List<LogItem> logItems) throws LogException, LogSizeTooLargeException {
        int sizeInBytes = LogSizeCalculator.calculate(logItems);
        ensureValidLogSize(sizeInBytes);
        List<Attempt> attempts = new ArrayList<>();
        PutLogsRequest request = buildPutLogsRequest(logItems, topicID);
        Map<String, String> headParameter = getCommonHeadPara();
        request.SetParam(Constants.TOPIC_ID, request.GetTopic());
        Map<String, String> urlParameter = request.GetAllParams();
        byte[] logBytes = request.GetLogGroupBytes(producerConfig.getSourceIp(), "");
        PutLogsResponse response = sendLogs(urlParameter, headParameter, logBytes);
        Attempt attempt = new Attempt(true, response.GetRequestId(), "", "", nowMs);
        attempts.add(attempt);
        return new Result(true, attempts, 1);
    }
}
