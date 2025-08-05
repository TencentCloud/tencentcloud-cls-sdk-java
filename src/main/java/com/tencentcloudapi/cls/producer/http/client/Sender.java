package com.tencentcloudapi.cls.producer.http.client;

import com.tencentcloudapi.cls.producer.common.Constants;
import com.tencentcloudapi.cls.producer.http.comm.OkHttpClientInstance;
import com.tencentcloudapi.cls.producer.http.comm.RequestMessage;
import com.tencentcloudapi.cls.producer.http.utils.HttpUtil;
import com.tencentcloudapi.cls.producer.response.PutLogsResponse;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Map;

/**
 * @author farmerx
 */
public class Sender {
    public static PutLogsResponse doPost(RequestMessage requestMessage) throws Exception {
        PutLogsResponse resp;
        // connection.setRequestProperty("connection", "Keep-Alive");
        RequestBody body = RequestBody.create(
                requestMessage.getContent(),
                MediaType.parse(Constants.CONST_PROTO_BUF));
        Request.Builder requestBuilder = new Request.Builder()
                .url(buildUri(requestMessage))
                .method(requestMessage.getMethod().toString(), body);
        requestBuilder.header("User-Agent", "cls-java-sdk-1.0.8");
        for (Map.Entry<String, String> entry : requestMessage.getHeaders().entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }

        Request request = requestBuilder.build();
        try (Response response = OkHttpClientInstance.okHttpClient.newCall(request).execute()) {
            resp = new PutLogsResponse(response.headers().toMultimap());
            resp.SetHttpStatusCode(response.code());
        }
        return resp;
    }

    /**
     * 构造uri
     * @param requestMessage message
     * @return String
     */
    private static String buildUri(RequestMessage requestMessage) {
        final String delimiter = "/";
        String uri = requestMessage.getEndpoint().toString();
        if (! uri.endsWith(delimiter)
                && (requestMessage.getResourcePath() == null ||
                ! requestMessage.getResourcePath().startsWith(delimiter))){
            uri += delimiter;
        }

        if (requestMessage.getResourcePath() != null){
            uri += requestMessage.getResourcePath();
        }

        String paramString = HttpUtil.paramToQueryString(requestMessage.getParameters(), Constants.UTF_8_ENCODING);
        if (paramString != null ) {
            uri += "?" + paramString;
        }
        return uri;
    }



}

