package com.baizhi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //指明当前注解所使用的位置
@Retention(RetentionPolicy.RUNTIME) //指定当前注解的生效时机,运行时生效
public @interface RedisCache {
}
