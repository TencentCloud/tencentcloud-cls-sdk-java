package com.tencentcloudapi.cls.producer.http.client;

import com.tencentcloudapi.cls.producer.common.Constants;
import com.tencentcloudapi.cls.producer.http.comm.RequestMessage;
import com.tencentcloudapi.cls.producer.http.utils.HttpUtil;
import com.tencentcloudapi.cls.producer.response.PutLogsResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author farmerx
 */
public class Sender {
    public static PutLogsResponse doPost(RequestMessage requestMessage) throws Exception {
        PutLogsResponse resp;
        HttpURLConnection connection;
        OutputStream outputStream;
        InputStream inputStream; // 输入流，用于接收响应
        URL url = new URL(buildUri(requestMessage));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMessage.getMethod().toString());
        connection.setConnectTimeout(Constants.HTTP_CONNECT_TIME_OUT);
        connection.setReadTimeout(Constants.HTTP_SEND_TIME_OUT);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // connection.setRequestProperty("connection", "Keep-Alive");
        for (Map.Entry<String, String> entry : requestMessage.getHeaders().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setRequestProperty("User-Agent", "cls-java-sdk-1.0.8");

        outputStream = connection.getOutputStream();
        outputStream.write(requestMessage.getContent());

        resp = new PutLogsResponse(connection.getHeaderFields());
        int statusCode = connection.getResponseCode();
        resp.SetHttpStatusCode(statusCode);

        if (statusCode >= 200 && statusCode < 300) {
            inputStream = connection.getInputStream();// 请求成功，从 getInputStream() 读取
        } else {
            inputStream = connection.getErrorStream();// 请求失败，从 getErrorStream() 读取
            if (inputStream == null) { // 如果未返回错误流，则使用输入流
                inputStream = connection.getInputStream();
            }
        }
        String responseBody = readInputStream(inputStream);
        resp.SetResponseBody(responseBody);

        inputStream.close();
        outputStream.close();

        return resp;
    }

    /**
     * 从输入流中读取ResponseBody
     * @param inputStream 输入流
     * @return 读取到的字符串内容
     */
    private static String readInputStream(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            return responseBuilder.toString();
        } catch (Exception e) {
            return "";
        }
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

