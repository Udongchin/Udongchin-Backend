package com.api.udc.global.security.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOrigins("http://localhost:5173","https://localhost:5173","https://2024-udongchin-front.vercel.app") // 허용할 출처. 모든 도메인 허용하려면 "*"
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true); // 인증 정보를 포함할 수 있도록 허용

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/images/**") // 프론트
                .addResourceLocations("file:/static/images/") // 백엔드
                .setCachePeriod(3600); // 캐시 기간 설정 (선택 사항)
    }
}