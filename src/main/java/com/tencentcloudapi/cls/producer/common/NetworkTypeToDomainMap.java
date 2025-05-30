package com.tencentcloudapi.cls.producer.common;

import java.util.HashMap;
import java.util.Map;

public class NetworkTypeToDomainMap {
    public static Map<String, String> networkTypeToDomainMap = new HashMap<>();

    static {
        networkTypeToDomainMap.put("内网", "cls.tencentyun.com");
        networkTypeToDomainMap.put("外网", "cls.tencentcs.com");
    }

    public static Map<String, String> getNetworkTypeToDomainMap() {
        return networkTypeToDomainMap;
    }
}
