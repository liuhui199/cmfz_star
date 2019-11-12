package com.baizhi.aop;

import com.alibaba.fastjson.JSONObject;
import com.baizhi.annotation.ClearRedisCache;
import com.baizhi.annotation.RedisCache;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.util.Set;

//@Configuration   //springboot注解
//@Aspect     //aop 切面注解
public class RedisCacheAop {
    @Autowired
    private Jedis jedis;

    //aop 切面注解  环绕通知
    @Around("execution(* com.baizhi.service.*.selectAll(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //判断目标方法上是否存在ReidsCache
        //如果存在,则需要做缓
        //如果不存在，则没有缓存，直接方法放行

        //获取目标方法
        Object target = proceedingJoinPoint.getTarget();//获取目标方法所在的 类的对象 target:com.baizhi.service.impl.BannerServiceImpl@193b3b18
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();//获取目标方法 Map com.baizhi.service.impl.BannerServiceImpl.selectAll(Integer,Integer)
        Object[] args = proceedingJoinPoint.getArgs();//获取目标方法的参数值
        //System.out.println(args); //page  rows
        Method method = methodSignature.getMethod();
        boolean b = method.isAnnotationPresent(RedisCache.class);
        if (b) {
            //目标方法上存在RedisCache注解
            //直接方法redis数据库,根据key获取对应的值
            //com.baizhi.service.impl.BannerServiceImpl.selectAll(1,3)
            String className = target.getClass().getName();
            String methodName = method.getName();
            StringBuilder sb = new StringBuilder();
            sb.append(className).append(".").append(methodName).append("(");
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i]);
                if (i == args.length - 1) {
                    break;
                }
                sb.append(",");
            }
            sb.append(")");
            String key = sb.toString();
            System.out.println("key:" + key);
            //判断redis缓存中是否存在这个key
            if (jedis.exists(key)) {
                String result = jedis.get(key);
                return JSONObject.parse(result);
            } else {
                Object result = proceedingJoinPoint.proceed();// 缓存中不含有该key,放行方法
                jedis.set(key, JSONObject.toJSONString(result));
                return result;
            }
        } else {
            //目标方法上不存在redisCache注解  直接放行
            Object result = proceedingJoinPoint.proceed();
            jedis.close();//关闭jedis的连接
            return result;
        }
    }

    //aop后置通知 执行增删改方法之后清除当前业务层的查询的缓存
    @After("execution(* com.baizhi.service.*.*(..)) && !execution(* com.baizhi.service.*.selectAll(..))")
    public void after(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        String className = target.getClass().getName(); //获取当前执行目标对象的全名

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        Method method = methodSignature.getMethod();
        boolean b = method.isAnnotationPresent(ClearRedisCache.class);
        if (b) {
            //清除缓存
            Set<String> keys = jedis.keys("*"); //获取redis缓存中的所有key
            for (String key : keys) {
                if (key.startsWith(className)) { //当前遍历的key 包含当前执行的目标方法的全类名（以当前执行目标方法的全类名开头）
                    jedis.del(key);//删除缓存中的这个key
                }
            }
            jedis.close();//关闭jedis的连接
        }
    }
}
