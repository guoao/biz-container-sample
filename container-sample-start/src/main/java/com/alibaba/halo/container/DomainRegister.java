package com.alibaba.halo.container;

import com.alibaba.biz.PluginManager;
import com.alibaba.halo.container.manage.ContainerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@Slf4j
public class DomainRegister {

    @EventListener(ContextRefreshedEvent.class)
    public void init(ContextRefreshedEvent event) throws Exception {
        //防止子容器refresh事件触发父容器处理
        if (event.getApplicationContext().getParent() != null) {
            return;
        }

        //初始化插件，包括初始化容器
        PluginManager.getInstance();

        //启动容器
        ContainerManager.start();
    }
}
