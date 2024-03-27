package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoillAspect {

    /**
     * 切入点
     */
//    execution(……)：根据方法的签名来匹配
//    @annotation(……) ：根据注解匹配
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}


    /**
     * 前置通知
     */
    @Before("autoFillPointCut()")
    public void aotoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的填充");
//        1.获取数据库当前操作的类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取方法签名对象
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = annotation.value();//获取数据库操作类型

//        2.获取当前方法执行的实体类对象
        Object[] args = joinPoint.getArgs(); //getArgs()获取方法执行时需要的参数
        if (args==null || args.length==0){
            return;
        }
        Object entity = args[0];

//        3.设置时间以及获取当前id
        LocalDateTime time = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

//        4.根据当前不同的操作类型，为对应的属性通过反射进行赋值
        if (operationType==OperationType.INSERT){
//            需要赋四个值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

//                通过反射为对象属性赋值
                setCreateTime.invoke(entity, time);
                setCreateUser.invoke(entity,id);
                setUpdateTime.invoke(entity,time);
                setUpdateUser.invoke(entity,id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (operationType==OperationType.UPDATE){
//            需要赋两个值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

//                通过反射为对象属性赋值
                setUpdateTime.invoke(entity,time);
                setUpdateUser.invoke(entity,id);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
