package com.hexin.aop;

import com.alibaba.fastjson.JSONObject;
import com.hexin.mapper.OperateLogMapper;
import com.hexin.pojo.OperateLog;
import com.hexin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 切面类（aop）
 */
@Slf4j
@Component
@Aspect//切面类
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.hexin.anno.Log)")//@Around环绕通知
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {

//        1.获取请求行的JWT令牌
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);//解析JWT令牌获得数据
//        2.获取操作人的id
        Integer operateUser = (Integer) claims.get("id");
//        3.获取 操作的类名
        String className = joinPoint.getTarget().getClass().getName();
//        4.获取操作的方法名
        String methodName = joinPoint.getSignature().getName();
//        5.获取方法的参数
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);
//        6.获取操作的时间
        LocalDateTime operateTime = LocalDateTime.now();

        long begin = System.currentTimeMillis();//记录目标方法运行开始前的毫秒值
//        调用原始目标方法运行
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis();//记录目标方法运行结束后的毫秒值

//        7.获取操作方法的返回值
        String returnValue = JSONObject.toJSONString(proceed);
//        8.获取操作的总耗时
        long costTime = end - begin;

//        记录操作日志
        OperateLog operatelog = new OperateLog(null, operateUser, operateTime, className, methodName, methodParams, returnValue, costTime);
        operateLogMapper.insert(operatelog);//调用mapper接口方法
        return proceed;

    }

}
