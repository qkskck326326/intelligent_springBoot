package org.ict.intelligentclass.security.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.handler.CustomLogoutHandler;
import org.ict.intelligentclass.security.jwt.filter.JwtRequestFilter;
import org.ict.intelligentclass.security.jwt.filter.LoginFilter;
import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.security.service.LoginTokenService;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration  // 클래스가 스프링의 구성(Configuration) 클래스임을 나타냄. 클래스 내에서 정의된 메소드들은 스프링 컨테이너에 의해 빈(Bean)으로 관리될 수 있음.
@EnableWebSecurity  // Spring Security를 활성화하여 웹 보안을 사용할 수 있게 설정함, 스프링 시큐리티 설정을 활성화함
//@RequiredArgsConstructor  //롬복(Lombok) 라이브러리의 어노테이션. final 또는 @NonNull 필드를 인자로 갖는 생성자를 자동으로 생성함.
@Slf4j  // 롬복(Lombok)을 사용하여 Logger를 자동으로 생성
public class SecurityConfig {
    // SecurityConfig 클래스는 HTTP 요청을 처리하기 위한 보안 규칙과 동작을 설정함.  // WebSecurityConfigurerAdapte를 상속받거나  WebSecurityConfigurerAdapte사용함.
    // Spring Security는 Spring 기반의 어플리케이션 보안(인증과 권한, 인가 등)을 담당하는 스프링 하위 프레임워크
    // Security는 인증과 권한에 대한 부분을 Filter의 흐름에 따라 처리해야 함.

    private final AuthenticationConfiguration authenticationConfiguration;  // 인증 및 권한 부여
    private final LoginTokenService loginTokenService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, LoginTokenService loginTokenService, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.loginTokenService = loginTokenService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 인증(Authentication) 관리자를 스프링 컨테이너에 Bean 으로 등록함. 인증 과정에서 중요한 클래스 임
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    // HTTP 관련 보안 설정을 정의함
    // SecurityFilterChain 을 Bean 으로 등록하고, http 요청에 대한 보안을 구성함
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // csrf(Cross-Site-Request-Forgery), form login, http basic 인증을 비활성화 함
                // 예 : <img src="http://kakao.developer.com/login/kakao.png">(Cross-Site-Request)를 사용 못하게 함
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable) // form 태그로 submit 해서 오는 로그인 사용 못 하게 함
                .httpBasic(AbstractHttpConfigurer::disable) // 시큐리티 제공 로그인을 사용 못하게 함
                // HTTP 요청에 대한 접근 권한을 설정함
                .authorizeHttpRequests(auth -> auth
                        // .requestMatchers("/**").permitAll() // 모든 경로에 대해 접근 허용


                        //.requestMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN") // '/notice' 경로에 대한 POST 요청은 ADMIN 역할을 가진 사용자만 가능합니다.

                        // 해당 경로들은 인증 없이 접근 가능합니다.
                        .requestMatchers("/users/user", "/login", "/logout", "/notice", "/reissue", "/kakao/**", "/naver").permitAll() // 태석
                        .requestMatchers("/categories/upper", "/packages/upperCategorypackageall", "/packages", "/packages/detail").permitAll() // 채림
                        .requestMatchers("/posts/top10", "/posts/list", "/posts/searchTitleOrContent", "/posts/searchlistByCategory").permitAll() // 도하
                        .requestMatchers("/announcement/**").permitAll() // 강
                        // .requestMatchers("/").permitAll() // 경민
                        // .requestMatchers("/").permitAll() // 시원
                        .requestMatchers("/itNewsBoard/**", "/itNewsSite/**").permitAll() // 건우
                        .requestMatchers("/file/view/*","/file/download/*").permitAll() // 공통?


                        /* 강사님 코드
                        // '/notice' path로 post 요청에 대해서는 'ADMIN' 롤 정보를 가진 사용자만 가능하다.
                        .requestMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/notice").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/boards").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/board").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/board").hasAnyRole("USER", "ADMIN")
                        // 추가해야됨   // 회원서비스의 마이페이지(GET), 내정보 수정(PUT), 탈퇴(PUT | DELETE) - 로그인 해야 접근 가능 => "USER" 롤 필요
                        // 아래는 인증 없이 접근 가능
                        .requestMatchers("/", "/api/auth/login", "reissue", "/members/**","/logout", "/notices/**", "/boards/**", "/board").permitAll()
                        */

                        .anyRequest().authenticated())  // 그 외의 모든 요청은 인증을 요구함
                // JWTFilter 와 LoginFilter 를 필터 체인에 등록함
                .addFilterBefore(new JwtRequestFilter(jwtTokenUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(userService, loginTokenService,
                        authenticationManager(authenticationConfiguration), jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                // 로그아웃 처리를 커스터마이징함
                .logout(logout -> logout
                        .addLogoutHandler(new CustomLogoutHandler(jwtTokenUtil, loginTokenService, userService))
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        }))

                // 세션 정책을 STATELESS 로 설정하고, 세션을 사용하지 않는 것을 명시함
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}