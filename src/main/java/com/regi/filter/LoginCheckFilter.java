package com.regi.filter;

import com.alibaba.fastjson.JSON;
import com.regi.common.BaseContext;
import com.regi.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("拦截到请求");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        boolean check = check(urls, requestURI);
        if(check){
            log.info("本次请求不需要处理");
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，id为:{}",request.getSession().getAttribute("employee"));
            long id = Thread.currentThread().getId();
            log.info("线程id为:{}",id);
            Long empId =(Long) request.getSession().getAttribute("employee");
            BaseContext.setCuerrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登录，id为:{}",request.getSession().getAttribute("user"));
            Long userId =(Long) request.getSession().getAttribute("user");
            BaseContext.setCuerrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }
    public boolean check(String[]urls,String requestURI){
        for (String url : urls) {
            boolean match = antPathMatcher.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
