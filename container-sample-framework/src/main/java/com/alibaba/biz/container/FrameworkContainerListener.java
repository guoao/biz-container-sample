package com.alibaba.biz.container;


import com.alibaba.biz.ExtensionCache;
import com.alibaba.biz.PluginManager;
import com.alibaba.halo.container.core.AbstractListener;
import com.alibaba.halo.container.core.Container;
import com.alibaba.halo.container.utils.CurrentContextClassLoaderRunner;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FrameworkContainerListener extends AbstractListener {
    @Override
    public void lifeCycleStarting(Container container) throws Exception {
        log.warn("container {} starting...",container.getName());
        CurrentContextClassLoaderRunner
                .create(container.getClassLoader())
                .run(() ->  PluginManager.getInstance().loadExtensions());
    }

    @Override
    public void lifeCycleStopping(Container container) throws Exception {
        List<String> extensionCodes = PluginManager.getInstance().loadExtensions();
        if(extensionCodes!=null && extensionCodes.size()>0){
            for(String extensionCode : extensionCodes){
                ExtensionCache.remove(extensionCode);
            }
        }
    }
}
