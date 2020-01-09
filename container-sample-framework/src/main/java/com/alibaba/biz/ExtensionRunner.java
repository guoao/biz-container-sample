package com.alibaba.biz;

import com.alibaba.halo.container.core.LifeCycleContainer;
import com.alibaba.halo.container.manage.ContainerManager;

public class ExtensionRunner {
    public static Object runExtension(String extensionCode){
        BizExtension extension = ExtensionCache.getExtension(extensionCode);
        if(extension==null){
            throw new RuntimeException("extension:"+extensionCode+" not exist");
        }
        ExtensionCallback callback = new ExtensionCallback() {
            @Override
            public Object apply(BizExtension extension) {
                return extension.run();
            }
            @Override
            public Object apply(Object o) {
                return null;
            }
        };

        ClassLoader extensionClassloader = extension.getClass().getClassLoader();
        LifeCycleContainer container = ContainerManager.retrieveContainerByClassLoader(extensionClassloader);
        if (container != null) {
            return container.run(()->callback.apply(extension));
        } else {
            return callback.apply(extension);
        }
    }
}
