package com.hexin.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 */
@Retention(RetentionPolicy.RUNTIME)//指定运行时有效
@Target(ElementType.METHOD)//指定作用在方法上
public @interface Log {
}
