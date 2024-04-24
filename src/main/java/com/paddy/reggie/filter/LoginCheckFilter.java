package com.paddy.reggie.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登陆 - 过滤器
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATTERN_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的URI
        String requestURI = request.getRequestURI();



        // 不需要处理的路径
        String[] urls = new String[]{
                "employee/login",
                "employee/logout",
                "backend/**",
                "front/**"
        };

        // 2. 本次请求是否要处理 - 是否需要登陆  哪些要登陆



        log.info("拦截到请求：{}", request.getRequestURI());
        filterChain.doFilter(servletRequest, response);
    }


}
