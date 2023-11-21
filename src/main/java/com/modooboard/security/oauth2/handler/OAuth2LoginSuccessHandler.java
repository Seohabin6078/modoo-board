//package com.modooboard.security.oauth2.handler;
//
//import com.modooboard.member.repository.MemberRepository;
//import com.modooboard.security.jwt.JwtTokenizer;
//import com.modooboard.security.oauth2.oauth2user.CustomOAuth2User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final JwtTokenizer jwtTokenizer;
//    private final MemberRepository memberRepository;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        log.info("OAuth2 Login 성공!");
//
//        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//
//        // todo 여기 부분 코드 수정 반드시 해야함!!
//        // 추가 정보를 받는 경우와 아닌 경우로 나눠야 하는데
//        // 추가 정보를 받는 경우도 결국 인증에는 성공한 것이기 때문에 토큰을 발급해줘야 하는데 어떻게 로직을 작성할 수 있을까??
//        if (oAuth2User.getRoles().get(0).equals("GUEST")) {
//            response.sendRedirect("/oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트
//            //todo 추가정보를 입력받는 컨트롤러, 서비스를 추가로 구현하고 그 시점에서 권한을 GUEST -> USER 로 변경하기
//        } else {
//            //todo 추가정보를 이미 입력한 경우라면 무조건 토큰을 발급해주는데 refresh 토큰의 유무나 상태에 따라 발급 방식을 바꾸는 방향으로 수정해보기!!
//            String accessToken = delegateAccessToken(oAuth2User);
//            String refreshToken = delegateRefreshToken(oAuth2User);
//
//            response.setHeader("Authorization", "Bearer " + accessToken);
//            response.setHeader("Refresh", refreshToken);
//        }
//    }
//
//    private String delegateAccessToken(CustomOAuth2User oAuth2User) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", oAuth2User.getEmail());
//        claims.put("roles", oAuth2User.getRoles());
//
//        String subject = oAuth2User.getEmail();
//        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
//
//        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//
//        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
//    }
//
//    private String delegateRefreshToken(CustomOAuth2User oAuth2User) {
//        String subject = oAuth2User.getEmail();
//        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
//        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//
//        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
//    }
//}
