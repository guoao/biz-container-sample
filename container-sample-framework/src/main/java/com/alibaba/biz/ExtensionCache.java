package com.alibaba.biz;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * extensions will be cached after application or container started
 */
public class ExtensionCache {
    private static Map<String,BizExtension> extensionMap = new ConcurrentHashMap<>();

    public static void doCache(String code, BizExtension extension){
       extensionMap.put(code,extension);
    }

    public static BizExtension getExtension(String extensionCode){
        return extensionMap.get(extensionCode);
    }

    public static BizExtension remove(String extensionCode){
        return extensionMap.remove(extensionCode);
    }
}
