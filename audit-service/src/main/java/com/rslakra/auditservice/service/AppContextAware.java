package com.rslakra.auditservice.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author Rohtash Lakra
 * @created 8/4/21 5:49 PM
 */
@Service
public class AppContextAware implements ApplicationContextAware {

    private static ApplicationContext APP_CONTEXT;

    /**
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APP_CONTEXT = applicationContext;
    }

    /**
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> beanClass) {
        return APP_CONTEXT.getBean(beanClass);
    }
}
