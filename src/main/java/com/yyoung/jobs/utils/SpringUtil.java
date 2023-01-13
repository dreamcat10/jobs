package com.yyoung.jobs.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null){
            SpringUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Object getBean(String name){
        if (name != null&& !name.equals("")){
            Object bean = getApplicationContext().getBean(name);
            return bean;
        }
        return null;
    }
    public static <T>Object getBean(Class<T> tClass){
        T bean = getApplicationContext().getBean(tClass);
        return bean;
    }

    public static <T>Object getBean(Class<T> class1,String name){
        if (name != null&& !name.equals("")){
            T bean = getApplicationContext().getBean(name,class1);
            return bean;
        }
        return null;
    }

}
