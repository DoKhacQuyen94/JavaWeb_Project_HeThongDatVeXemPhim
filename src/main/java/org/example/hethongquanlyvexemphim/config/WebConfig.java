package org.example.hethongquanlyvexemphim.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.hethongquanlyvexemphim.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                User user = (User) request.getSession().getAttribute("user");
                // Chặn User thường vào link Admin
                if (user!= null && request.getRequestURI().startsWith("/admin") && !user.getRole().equalsIgnoreCase("admin")) {
                    response.sendRedirect("/home?error=denied");
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/admin/**", "/user/**", "/home", "/profile");
    }
}