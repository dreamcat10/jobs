package com.yyoung.jobs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Demo1 {

    Integer[] arr = {1,2,3};
    static String name = "empId";

    public static void main(String[] args) {
        System.out.println(name.contains("Id"));
    }
}