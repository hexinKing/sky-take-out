package com.hexin.filter;

import com.alibaba.fastjson.JSONObject;
import com.hexin.pojo.Result;
import com.hexin.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter过滤器
 */
@Slf4j
//@WebFilter(urlPatterns = "/*")//配置拦截资源路径   --/* :所有路径
public class AFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//初始化方法，web服务器调用启动,创建Filter时调用
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//拦截请求时，调用该方法，可调用多次
//        先对servletRequest,servletResponse进行强制转换
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

//        获取请求的访问路径url
        String url = req.getRequestURL().toString();
        log.info("请求的url: {}", url);
//        if判断是否为登录的路径，是则放行，contains:判断其中是否含有"Login"
        if (url.contains("login")) {
            log.info("登录操作,放行");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

//        获取客服端中的请求头中的令牌(token)
        String jwt = req.getHeader("token");

//        判断令牌是否存在,如果不存在,发送错误信息
//        StringUtils.hasLength(jwt) 工具类判断jwt是否有长度
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头信息为空,放回未登录的信息");
            Result result = Result.error("NOT_LOGIN");
//            通过阿里巴巴下的转换json工具.进行手动转换
            String notLogin = JSONObject.toJSONString(result);
//            返回错误信息
            resp.getWriter().write(notLogin);
            return;
        }

//        解析校验令牌是否正确,同时捕获异常
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {//解析失败
            e.printStackTrace();//输入错误信息到控制台
            log.info("解析令牌错误，返回未登录错误信息");
            Result error = Result.error("NOT_LOGIN");
//            通过阿里巴巴下的转换json工具.进行手动转换
            String notLogin = JSONObject.toJSONString(error);
//            返回错误信息
            resp.getWriter().write(notLogin);
            return;
        }

//       令牌存在且无误，放行
        log.info("令牌存在且无误，放行");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
//销毁方法，服务器关闭时调用，只调用一次
    }
}
