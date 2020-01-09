package com.alibaba.biz.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Spring bean工具
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	private ApplicationContextUtil(){
	}

	@Override
	public void setApplicationContext(ApplicationContext context){
		applicationContext = context;
	}

	/**
	 *
	 * 取得指定bean
	 *
	 * @version 1.0
	 * @since 2015年2月2日
	 *
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName){
		return applicationContext.getBean(beanName);
	}

	/**
	 * 根据类名获取bean
	 * @param clazz
	 * @return
	 */
	public static  <T> Collection<T> getBeans(Class<T> clazz) {
		Map<String, T> beanMaps = applicationContext.getBeansOfType(clazz);
		if (beanMaps != null && !beanMaps.isEmpty()) {
			return beanMaps.values();
		} else {
			return null;
		}
	}

	public static ApplicationContext getContext(){
		return applicationContext;
	}
}

