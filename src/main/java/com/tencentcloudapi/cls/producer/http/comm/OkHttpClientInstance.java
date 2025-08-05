package com.tencentcloudapi.cls.producer.http.comm;

import com.tencentcloudapi.cls.producer.common.Constants;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class OkHttpClientInstance {

    public static final OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(Constants.HTTP_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.HTTP_SEND_TIME_OUT, TimeUnit.MILLISECONDS)
                .build();
    }

}
