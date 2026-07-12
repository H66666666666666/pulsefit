package com.itbin.interceptor;

import com.itbin.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取到请求路径

        String url = request.getRequestURI();
        log.info("请求路径: {}", url);

        //2.判断是否是登陆请求或静态资源，如果是则放行

        // 放行登录请求
        if (url.contains("/login")) {
            log.info("登录请求，放行");
            return  true;
        }

        // 放行静态资源（可选，根据实际需求配置）
//        if (url.startsWith("/static/") || url.startsWith("/css/") ||
//            url.startsWith("/js/") || url.startsWith("/img/") ||
//            url.startsWith("/index")) {
//            log.info("静态资源请求，放行: {}", url);
//            filterChain.doFilter(request, response);
//            return;
//        }
        //3,获取请求头中的token

        String token = request.getHeader("token");
        log.info("token值: {}", token);

        //4.判断token是否存在，如果不存在，说明用户没有登录，返回错误信息（响应401状态码）

        if (token == null|| token.isEmpty()) {
            log.info("令牌为空，响应401");
            response.setStatus(401);
            return false;

        }

        //5.令牌存在，校验令牌，如果验证失败，则返回错误信息（响应401状态码）

        try {
            JwtUtils.parseToken(token);//校验令牌
        } catch (Exception e) {
            log.info("令牌验证失败，响应401");
            response.setStatus(401);
            return  false;
        }

        //6.校验成功，放行
        log.info("令牌验证成功，放行");
        return true;
    }
}