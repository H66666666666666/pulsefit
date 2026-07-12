package com.itbin.filter;

import com.itbin.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取到请求路径

        String url = request.getRequestURI();
        log.info("请求路径: {}", url);

        //2.判断是否是登陆请求或静态资源，如果是则放行

        // 放行登录请求
        if (url.contains("/login")) {
            log.info("登录请求，放行");
            filterChain.doFilter(request, response);
            return;
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

        //4.判断token是否存在，如果不存在，说明用户没有登录，返回错误信息（响应401状态码）

        if (token == null|| token.isEmpty()) {
            log.info("令牌为空，响应401");
            response.setStatus(401);
            return;

        }

        //5.令牌存在，校验令牌，如果验证失败，则返回错误信息（响应401状态码）

        try {
            JwtUtils.parseToken(token);//校验令牌
        } catch (Exception e) {
            log.info("令牌验证失败，响应401");
            response.setStatus(401);
            return;
        }

        //6.校验成功，放行
        filterChain.doFilter(request, response);
    }
}