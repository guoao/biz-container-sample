package com.alibaba.halo;

import com.alibaba.biz.BizExtension;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;

public class ServiceLoaderTest {
    public static void main(String[] args) {
        try {
            URL url = new File("/Users/guoao/work/biz-extension/extension-sample2/target/extension-sample2-1.0-SNAPSHOT.jar").toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            ServiceLoader<BizExtension> serviceLoader = ServiceLoader.load(BizExtension.class,classLoader);
            for(BizExtension extension : serviceLoader){
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
