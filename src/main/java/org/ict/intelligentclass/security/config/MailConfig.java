package org.ict.intelligentclass.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.naver.com"); // 메일 서버 호스트 설정 (네이버 SMTP 서버)
        mailSender.setPort(465); // 메일 서버 포트 설정

        mailSender.setUsername("officialintelliclass@naver.com"); // 발신 이메일 계정
        mailSender.setPassword("7W99PXF8JF9H"); // 애플리케이션 비밀번호

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp"); // 메일 전송 프로토콜 설정 (SMTP)
        props.put("mail.smtp.auth", "true"); // SMTP 인증 사용 설정
        props.put("mail.smtp.starttls.enable", "true"); // TLS 사용 설정
        props.put("mail.smtp.starttls.required", "true"); // TLS 필수 설정
        props.put("mail.smtp.ssl.enable", "true"); // SSL 사용 설정
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 사용할 SSL/TLS 프로토콜 설정
        props.put("mail.smtp.ssl.trust", "smtp.naver.com"); // 신뢰할 SMTP 서버 설정
        // props.put("mail.debug", "true"); // 디버깅 모드 활성화

        return mailSender;
    }
}