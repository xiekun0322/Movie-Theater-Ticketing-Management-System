package com.starscreen.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 * 未登录用户访问 /orders、/order/** 时，跳转到 /login
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();

        // 静态资源、登录页、首页、登录 API 等放行
        if (uri.startsWith("/login")
                || uri.startsWith("/api/user/login")
                || uri.startsWith("/api/user/register")
                || uri.equals("/")
                || uri.startsWith("/movie/")
                || uri.startsWith("/cinemas/")
                || uri.startsWith("/seat/")
                || uri.startsWith("/api/seats/")
                || uri.startsWith("/images/")
                || uri.startsWith("/css/")
                || uri.startsWith("/js/")
                || uri.startsWith("/favicon")
                || uri.startsWith("/test")) {
            return true;
        }

        // 检查 session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            return true;
        }

        // 未登录
        if (uri.startsWith("/api/")) {
            // API 返回 401 JSON
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"请先登录\",\"data\":null}");
            return false;
        } else {
            // 页面跳转登录页
            response.sendRedirect("/login");
            return false;
        }
    }
}
