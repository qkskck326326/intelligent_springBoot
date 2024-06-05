package org.ict.intelligentclass.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// url요청을 위한 설정파일
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // CORS 관련 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로에 대해 CORS 설정을 추가
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                // GET, POST, PUT, DELETE, HEAD, OPTIONS 메소드 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                // 모든 HTTP 헤더 요청 허용
                .allowedHeaders("*")
                // 쿠키나 인증과 관련된 정보를 포함한 요청 허용
                .allowCredentials(true);
    }
}
