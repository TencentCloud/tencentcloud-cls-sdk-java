package com.tencentcloudapi.cls.producer.common;

import java.util.HashMap;
import java.util.Map;

public class RegionToDomainMap {
    public static Map<String, String> regionToDomainMap = new HashMap<>();

    static {
        regionToDomainMap = new HashMap<String, String>();
        regionToDomainMap.put("北京", "ap-beijing");
        regionToDomainMap.put("广州", "ap-guangzhou");
        regionToDomainMap.put("上海", "ap-shanghai");
        regionToDomainMap.put("成都", "ap-chengdu");
        regionToDomainMap.put("南京", "ap-nanjing");
        regionToDomainMap.put("重庆", "ap-chongqing");
        regionToDomainMap.put("中国香港", "ap-hongkong");
        regionToDomainMap.put("硅谷", "na-siliconvalley");
        regionToDomainMap.put("弗吉尼亚", "na-ashburn");
        regionToDomainMap.put("新加坡", "ap-singapore");
        regionToDomainMap.put("曼谷", "ap-bangkok");
        regionToDomainMap.put("法兰克福", "eu-frankfurt");
        regionToDomainMap.put("东京", "ap-tokyo");
        regionToDomainMap.put("首尔", "ap-seoul");
        regionToDomainMap.put("雅加达", "ap-jakarta");
        regionToDomainMap.put("圣保罗", "sa-saopaulo");
        regionToDomainMap.put("深圳金融", "ap-shenzhen-fsi");
        regionToDomainMap.put("上海金融", "ap-shanghai-fsi");
        regionToDomainMap.put("北京金融", "ap-beijing-fsi");
        regionToDomainMap.put("上海自动驾驶云", "ap-shanghai-adc");
    }

    public static Map<String, String> getRegionToDomainMap() {
        return regionToDomainMap;
    }
}
