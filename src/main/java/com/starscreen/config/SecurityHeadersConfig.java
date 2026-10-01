package com.starscreen.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 安全响应头配置
 * 修复 ZAP 扫描发现的 3 个告警：
 * - Content Security Policy (CSP) Header Not Set
 * - Missing Anti-clickjacking Header
 * - X-Content-Type-Options Header Missing
 */
@Configuration
public class SecurityHeadersConfig {

    @Bean
    public FilterRegistrationBean<Filter> securityHeadersFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Filter() {
            @Override
            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletResponse response = (HttpServletResponse) res;

                // 1. 防止点击劫持
                response.setHeader("X-Frame-Options", "DENY");

                // 2. 防止 MIME 类型猜测
                response.setHeader("X-Content-Type-Options", "nosniff");

                // 3. Content Security Policy
                response.setHeader("Content-Security-Policy",
                        "default-src 'self'; " +
                        "img-src 'self' data:; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "script-src 'self' 'unsafe-inline'; " +
                        "font-src 'self' data:; " +
                        "connect-src 'self'; " +
                        "frame-ancestors 'none'");

                // 4. XSS 保护（老浏览器）
                response.setHeader("X-XSS-Protection", "1; mode=block");

                // 5. Referrer 策略
                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

                chain.doFilter(req, res);
            }
        });
        registration.addUrlPatterns("/*");
        registration.setOrder(1);   // 最先执行
        return registration;
    }
}
