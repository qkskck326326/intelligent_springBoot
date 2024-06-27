package org.ict.intelligentclass.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * Configuration 어노테이션: 부트에게 이게 설정파일임을 알림
 * EnableWebSocketMessageBroker : 웹소켓 설정 활성화
 * 이 클래스는 스프링이 웹소켓 커뮤니케이션을 세팅하기 위한 클래스
 * */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Overridden 메소드
     * 메세지 브로커 /topic에 접두사 /app으로 접두어 설정
     * */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        //클라이언트로 부터 보내진 메세지들의 접두어 설정
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Overridden 메소드
     * 웹소켓 엔드포인트 설정 및 크로스오리진 설정
     * */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //사용자들이 웹소켓 서버에 연결하기 위해 사용할 웹소켓 엔드포인트 설정
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:3000") // 크로스 오리진 설정
                .withSockJS(); //fallback 옵션 설정
    }
}
