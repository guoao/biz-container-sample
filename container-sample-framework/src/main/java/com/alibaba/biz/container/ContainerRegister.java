package com.alibaba.biz.container;

import com.alibaba.biz.util.ApplicationContextUtil;
import com.alibaba.halo.container.core.ContainerType;
import com.alibaba.halo.container.isolate.TenantContainerFactory;
import com.alibaba.halo.container.manage.ContainerManager;
import com.alibaba.halo.container.manage.LifeCycleContainerFactory;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

@Slf4j
public class ContainerRegister {

    public static void register(){
        initContainer(ContainerType.APP);
        initContainer(ContainerType.PRODUCT);
    }

    /**
     * init product container
     *
     * @throws Exception
     */
    private static void  initContainer(ContainerType containerType)  {
        if (ContainerManager.isEnableContainer()) {
            List<String> paths = Lists.newArrayList();
            if(containerType==ContainerType.PRODUCT){
                paths =  ContainerManager.getProductPath();
            }else if(containerType == ContainerType.APP){
                paths = ContainerManager.getContainerPath();
            }
            for (String path : paths) {
                log.warn("[CONTAINER_REGISTER] start register container from path:"+path);
                File dir   = new File(path);
                File[] files = dir.listFiles((file, name) -> name.toLowerCase().endsWith(".jar"));
                if (files == null || files.length <= 0) {
                    continue;
                }
                String containerName = StringUtils.substringAfterLast(path, File.separator);
                LifeCycleContainerFactory factory = TenantContainerFactory.newInstance()
                        .setName(containerName)
                        .setPath(path)
                        .setType(containerType)
                        .setParentApplicationContext(ApplicationContextUtil.getContext())
                        .addLifeCycleListener(new FrameworkContainerListener());
                ContainerManager.createContainer(factory);
                log.warn("[CONTAINER_REGISTER] container create finish, containerName:"+containerName);
            }
        }
    }
}
