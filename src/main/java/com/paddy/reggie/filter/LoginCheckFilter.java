package com.paddy.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.paddy.reggie.common.R;
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

        // 不需要处理的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        //  1. 获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);

        // 2. 本次请求是否要处理 - 是否需要登陆  哪些要登陆
        boolean check = check(urls, requestURI);

        // 3. 如果不需要处理，直接放行
        // true -> 不需要处理
        if (check){
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(servletRequest, response);
            return;
        }

        // 4. 判断登录状态，如果已登录，则直接放行
        Object employee = request.getSession().getAttribute("employee");
        if(employee != null){
            log.info("用户已登录，用户id为：{}", employee);
            filterChain.doFilter(servletRequest, response);
            return;
        }

        // 5. 如果未登陆则返回未登陆结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("用户未登录");
        return;
    }

    /**
     * 路径匹配，判断本次请求是否需要处理
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATTERN_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
