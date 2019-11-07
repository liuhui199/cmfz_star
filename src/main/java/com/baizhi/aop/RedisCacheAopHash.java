package com.baizhi.aop;

import com.alibaba.fastjson.JSONObject;
import com.baizhi.annotation.ClearRedisCache;
import com.baizhi.annotation.RedisCache;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;

@Configuration
@Aspect
public class RedisCacheAopHash {
    @Autowired
    private Jedis jedis;

    @Around("execution(* com.baizhi.service.*.selectAll(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object target = proceedingJoinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();

        Method method = methodSignature.getMethod();
        boolean b = method.isAnnotationPresent(RedisCache.class);
        if (b) {
            //目标方法上存在该注解
            String className = target.getClass().getName();
            StringBuilder sb = new StringBuilder();
            String methodName = method.getName();
            sb.append(methodName).append("(");
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i]);
                if (i == args.length - 1) {
                    break;
                }
                sb.append(",");
            }
            sb.append(")");
            if (jedis.hexists(className, sb.toString())) {
                //判断redis中是否存在对应的key
                String result = jedis.hget(className, sb.toString());
                return JSONObject.parse(result);
            } else {
                Object result = proceedingJoinPoint.proceed();
                jedis.hset(className, sb.toString(), JSONObject.toJSONString(result));
                return result;
            }
        } else {
            //不存在该注解
            Object result = proceedingJoinPoint.proceed();
            return result;
        }
    }

    @After("execution(* com.baizhi.service.*.*(..)) && !execution(* com.baizhi.service.*.selectAll(..))")
    public void after(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String className = target.getClass().getName();
        if (method.isAnnotationPresent(ClearRedisCache.class)) {
            jedis.del(className);
        }
    }
}
