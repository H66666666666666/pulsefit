package com.itbin.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {


    //初始化方法，web服务器启动是执行，只执行一次
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init 初始化");
    }


    //拦截到请求后，执行，会执行多次
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("拦截到了请求");
        filterChain.doFilter(servletRequest, servletResponse);
    }


    //销毁方法，web服务器停止时执行，只执行一次
    @Override
    public void destroy() {
        log.info("destroy 销毁");
    }
}