package org.ict.intelligentclass.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.dto.CustomUserDetails;
import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
// Spring security의 OncePerRequestFilter 를 상속받음
//     >> 모든 요청에 대해 한번씩 실행되는 필터가 됨.
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    // 생성자를 통해  멤버변수 의존성 주입함.
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil=jwtTokenUtil;
    }

    @Override // HttpServletRequest와 HttpServletResponse를 낚아와서 검증하고 FilterChain으로 보냄.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 (request)에서 'Authorization' 해더를 추출함
        String authorizationHeader = request.getHeader("Authorization");

        String requestURI = request.getRequestURI();
        if ("/reissue".equals(requestURI)) { // 클라이언트가 토큰 재발행 요청을 했다
            // 찾아갈 대상으로 그대로 넘김
            filterChain.doFilter(request, response);    // 해당 요청을 처리할 컨트롤러 메소드로 전달해라. "/reissue" 에게로 보내라.

            return;
        }// 토큰 확인이 필요없는 요청 (로그인하지 않고 이용하는 서비스 url)은 그대로 다음 단계로 넘김


        // 요청 URI를 가져옴
        if ("/logout".equals(requestURI)) {  // '/logout' 요청은 인증 검사를 하지 않음
            // 찾아갈 uri(대상)으로 그대로 넘김
            filterChain.doFilter(request, response);

            return;
        }


        // 토큰확인이 필요없는 요청(로그인하지않아도 이용하는 서비스 url)은 그대로 다음단계로 넘김.
        // 'Authorization'이 해더에 없거나 Bearer 토큰이 아니면 요청을 계속 진행함.
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {  // Bearer로 시작하지않는다면, 토큰이 없다면
            filterChain.doFilter(request, response);
            return;
        }

        // authorization : 클라이언트가 서버에 인증 정보를 전달하는 데 사용되는 HTTP 헤더임.
        //  >>주로 API 요청 시, 클라이언트가 자신이 인증된 사용자임을 증명하기 위해 사용됨
        //  >>일반적으로 Bearer 토큰을 통해 전달되는 경우가 많음

        // Bearer 토큰에서 JWT를 추출함. (토큰 정보가 request해더에 있는 경우)
        // authorization이 가진 값 : "Bearer 토큰 문자열"
        String tokenValue = authorizationHeader.split(" ")[0]; // authorizationHeader의 첫번째가 토큰임

        //토큰만료 여부확인, 만료시 다음 필터로 넘기지 않음.
        if(jwtTokenUtil.isTokenExpired(tokenValue)){ // true: 만료
            //response body
            PrintWriter writer = response.getWriter();
            writer.println("invalid access token : JwtRequestFilter에서 isTokenExpired(tokenValue) 처리함");

            // response status code
            // 응답코드를 front와 맞추는 부분 401에러 외 다른 상태코드로 맞추면
            // 리프레시 토큰 발급 체크를 좀 더 빠르게 할 수 있음.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // token에서 category 가져오기
        String tokenType = jwtTokenUtil.getTokenTypeFromToken(tokenValue);
        // 토큰 category 가 access 가 아니 라면 만료 된 토큰 이라고 판단
        if (!tokenType.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token : JwtRequestFilter에서 !tokenType.equals(\'access\')처리함");

            //response status code
            // 응답 코드를 프론트와 맞추는 부분 401 에러 외 다른 코드로 맞춰서
            // 진행하면 리프레시 토큰 발급 체크를 빠르게 할수 있음
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 위의 조건들에 해당되지 않으면, 정상적인 만료되지않은 access토큰으로 요청이 왔다면
        // 토큰에서 이메일, 제공자, 유저타입 추출함
        UserId userId = jwtTokenUtil.getUserIdFromToken(tokenValue);
        int userType = jwtTokenUtil.getUserTypeFromToken(tokenValue);

        // 인증에 사용할 임시 User객체를 생성하고 추출한 정보 저장함
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUserType(userType);
        userEntity.setUserPwd("tempPassword");  //실제 인증에서는 사용되지 않는 임시 비밀번호를 설정함.


        // UserDto(회원정보) 객체를 기반으로 CustromUserDetails객체를 생성
        // Security가 제공하는 UserDetails를 상속받은 인증용 클래스롤 만든 것임 : security.jwt.model.CustomUserDetails
        //인증에 사용할 user를 기반으로 한 CustomUserDetail 객체가 필요함
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //Spring Security의 Authentication 객체를 생성하고, SecurityContext에 등록 설정함.
        //이것으로 해당 요청에 대한 사용자 인증이 완료됨.
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        // 액세스 토큰(로그인상태 확인)이 이상이 없다면 처리할 내용임




//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            try {
//                Claims claims = jwtTokenUtil.getClaimsFromToken(jwt);
//                userEmail = claims.getSubject();
//                provider = claims.get("provider", String.class);
//            } catch (Exception e) {
//                log.error("JWT token extraction error: ", e);
//            }
//        }
//
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail, provider);
//
//            if (jwtTokenUtil.validateToken(jwt)) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }

        filterChain.doFilter(request, response);
    }
}