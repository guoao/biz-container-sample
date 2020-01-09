package com.alibaba.biz;

import com.alibaba.biz.anotation.ExtensionCode;
import com.alibaba.biz.container.ContainerRegister;
import com.alibaba.halo.container.core.ContainerClassLoader;
import com.alibaba.halo.container.core.LifeCycleContainer;
import com.alibaba.halo.container.manage.ContainerManager;
import com.alibaba.halo.container.meta.JarInfo;
import com.google.common.collect.Lists;
import com.taobao.pandora.boot.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.ServiceLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Slf4j
public class PluginManager {

    private static volatile PluginManager instance = null;

    public static PluginManager getInstance() {
        PluginManager pluginManager = instance;
        if (pluginManager == null) {
            synchronized (PluginManager.class) {
                pluginManager = instance;
                if (pluginManager == null) {
                    pluginManager = new PluginManager();
                    try {
                        pluginManager.init();
                        instance = pluginManager;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return pluginManager;
    }

    private void init()  {
        initContainer();
        loadExtensions();
    }

    public List<String> loadExtensions()  {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        log.warn("load extension by classloader:{}",classLoader.getClass().getName());
        List<String> extensionCodes = Lists.newArrayList();

        if(classLoader instanceof ContainerClassLoader){
            //container spi 似乎扫不出来. To check..
            try {
                LifeCycleContainer container =
                        ContainerManager.retrieveContainerByClassLoader(classLoader);
                String interfaceName = BizExtension.class.getName();
                for (JarInfo jarInfo : container.getJarInfoList()) {
                    JarFile jarFile = new JarFile(jarInfo.getPath());
                    for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) {
                        JarEntry jarEntry = e.nextElement();
                        if (jarEntry.getName().equals("META-INF/services/" + interfaceName)) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarEntry)));
                            String clazzName = reader.readLine();
                            Class clazz = Class.forName(clazzName, true, classLoader);
                            BizExtension extension = (BizExtension) clazz.newInstance();
                            ExtensionCode annotation = extension.getClass().getAnnotation(ExtensionCode.class);
                            if (annotation != null) {
                                String extensionCode = annotation.code();
                                extensionCodes.add(extensionCode);
                                ExtensionCache.doCache(extensionCode, extension);
                                log.warn("cached extension by container:{},code:{}", extension.getClass().getName(), extensionCode);
                            }
                        }
                    }
                }
            }catch (Exception e){
                log.error("load extension error",e);
            }

        }else {
            ServiceLoader<BizExtension> serviceLoader =
                    ServiceLoader.load(BizExtension.class, classLoader);

            for (BizExtension extension : serviceLoader) {
                ExtensionCode annotation = extension.getClass().getAnnotation(ExtensionCode.class);
                if (annotation != null) {
                    String extensionCode = annotation.code();
                    extensionCodes.add(extensionCode);
                    ExtensionCache.doCache(extensionCode, extension);
                    log.warn("cached extension by service loader:{},code:{}", extension.getClass().getName(), extensionCode);
                }
            }
        }
        return extensionCodes;
    }

    private void initContainer(){
        ContainerRegister.register();
    }
}
