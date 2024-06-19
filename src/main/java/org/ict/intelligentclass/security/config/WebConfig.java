package org.ict.intelligentclass.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {  // 상속받음. WebMvcConfigurer메서드를 사용하여 CORS관련 설정을 추가하는 데 사용됨.
    // 웹 애플리케이션의 CORS(Cross-Origin Resource Sharing) 정책을 구성함.
    // 이 설정은 다른 도메인, 프로토콜 또는 포트를 가진 웹 페이지에서 현재 도메인의 리소스에 접근할 수 있도록 허용하거나 제한하는 역할을 함.
    // WebMvcConfigurer 인터페이스의 메소드를 오버라이드하여 스프링 MVC의 웹 설정을 사용자 정의할 수 있음.


    // React, View, Angular와 애플리케이션을 합쳐서 실행할 때, cross origin문제 발생처리가 목적임.
    // 참고 : 하나의 웹 애플리케이션 구동시 port한개로 구동이 원칙임
    // React 애플리케이션 port에서 요청 <---> Boot애플리케이션 port응답

    // WebMvcConfigurer인테페이스에서 오버라이딩한 메소드로, CORS관련 설정을 추가하는 데 사용됨.
    @Override
    public void addCorsMappings(CorsRegistry registry) {  // CORS 관련 설정을 정의
        // 모든 경로에 대한 CORS 설정을 추가한다는 의미.(/** : 외부에서 오는 모든경로),
        registry.addMapping("/**") // 이 설정은 모든 URL 패턴(/**)에 CORS 정책을 적용하겠다는 의미
                // 오직 "http://localhost:3000" 이 오리진에서 오는 요청만 허용한다는 의미임.
                // 개발 단계에서는 프론트엔드 서버의 주소가 됨.
                .allowedOrigins("http://localhost:3000")
                //.allowedOrigins("*") // 모든 도메인에서의 요청을 허용

                // 해당 오리진에서 허용할 http 메소드를 지정함.
                // GET, POST, PUT, DELETE, HEAD, OPTIONS 메소드만 허용함.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")

                // 모든 HTTP헤더를 요청에서 허용함.
                .allowedHeaders("*")  // Authorization과 content_type 뿐만이 아니라 모두 허용함.
                //.allowedHeaders("Authorization", "Content-Type") // 서버가 허용할 헤더를 지정합니다. 여기서는 "Authorization"과 "Content-Type" 헤더를 허용


                // 쿠키 또는 인증과 관련된 정보( HTTP 인증과 SSL 인증을 포함)를 포함한 요청을 허용함.
                // 브라우저가 응답에서 쿠키나 인증 관련 헤더를 읽을 수 있도록 합니다. true로 설정하면 크로스-도메인 요청에서도 사용자 인증 정보를 전송할 수 있습니다.
                .allowCredentials(true);


                //.maxAge(3600); // 3600초 = 1시간, 브라우저가 CORS 관련 설정을 캐시할 시간을 초 단위로 지정
    }
}
