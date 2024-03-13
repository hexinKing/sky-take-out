package com.hexin.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hexin.pojo.Result;
import com.hexin.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注册拦截器
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override//目标资源方法执行前执行，true：放行，false：不放行
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

//        获取请求的访问路径url
        String url = req.getRequestURL().toString();
        log.info("请求的url: {}", url);
//        if判断是否为登录的路径，是则放行，contains:判断其中是否含有"Login"
        if (url.contains("login")) {
            log.info("登录操作,放行");
            return true;
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
            return false;
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
            return false;
        }

//       令牌存在且无误，放行
        log.info("令牌存在且无误，放行");
        return true;
    }

    @Override//目标资源执行后执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override//视图渲染后执行，也是最后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
