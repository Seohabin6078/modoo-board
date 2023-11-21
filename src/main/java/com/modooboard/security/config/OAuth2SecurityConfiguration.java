//package com.modooboard.security.config;
//
//import com.modooboard.member.service.MemberService;
//import com.modooboard.security.filter.JwtVerificationFilter;
//import com.modooboard.security.handler.MemberAccessDeniedHandler;
//import com.modooboard.security.handler.MemberAuthenticationEntryPoint;
//import com.modooboard.security.oauth2.handler.OAuth2MemberAuthenticationSuccessHandler;
//import com.modooboard.security.jwt.JwtTokenizer;
//import com.modooboard.security.oauth2.oauth2user.CustomOAuth2UserService;
//import com.modooboard.security.oauth2.handler.OAuth2MemberAuthenticationFailureHandler;
//import com.modooboard.security.utils.CustomAuthorityUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@RequiredArgsConstructor
////@Configuration
//public class OAuth2SecurityConfiguration {
//    // 아래의 프로퍼티 정보들을 이용하여 ClientRegistrationRepository을 빈으로 등록하는 코드는 생략해도 알아서 스프링이 자동구성한다.
////    @Value("${spring.security.oauth2.client.registration.google.clientId}")
////    private String clientId;
////
////    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
////    private String clientSecret;
//
//    private final JwtTokenizer jwtTokenizer;
//    private final CustomAuthorityUtils authorityUtils;
//    private final MemberService memberService;
//    private final CustomOAuth2UserService customOAuth2UserService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf().disable()
////                .formLogin().disable()
////                .httpBasic().disable()
////                .authorizeHttpRequests(authorize -> authorize
////                        .anyRequest().authenticated()
////                )
////                .oauth2Login(withDefaults());
////        return http.build();
//
//        http
//                .headers().frameOptions().sameOrigin()
//                .and()
//                .csrf().disable()
//                .cors(withDefaults())
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
//                .accessDeniedHandler(new MemberAccessDeniedHandler())
//                .and()
//                .apply(new CustomFilterConfigurer())
//                .and()
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().authenticated()
////                        .antMatchers(HttpMethod.POST, "/*/members").permitAll()    // OAuth 2로 로그인하므로 회원 정보 등록 필요 없음.
////                        .antMatchers(HttpMethod.PATCH, "/*/members/**").hasRole("USER") // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
////                        .antMatchers(HttpMethod.GET, "/*/members").hasRole("ADMIN")  // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
////                        .antMatchers(HttpMethod.GET, "/*/members/**").hasAnyRole("USER", "ADMIN")  // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
////                        .antMatchers(HttpMethod.DELETE, "/*/members/**").hasRole("USER") // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
////                        .antMatchers(HttpMethod.POST, "/*/coffees").hasRole("ADMIN")
////                        .antMatchers(HttpMethod.PATCH, "/*/coffees/**").hasRole("ADMIN")
////                        .antMatchers(HttpMethod.GET, "/*/coffees/**").hasAnyRole("USER", "ADMIN")
////                        .antMatchers(HttpMethod.GET, "/*/coffees").permitAll()
////                        .antMatchers(HttpMethod.DELETE, "/*/coffees").hasRole("ADMIN")
////                        .antMatchers(HttpMethod.POST, "/*/orders").hasRole("USER")
////                        .antMatchers(HttpMethod.PATCH, "/*/orders").hasAnyRole("USER", "ADMIN")
////                        .antMatchers(HttpMethod.GET, "/*/orders/**").hasAnyRole("USER", "ADMIN")
////                        .antMatchers(HttpMethod.DELETE, "/*/orders").hasRole("USER")
////                        .anyRequest().permitAll()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .successHandler(new OAuth2MemberAuthenticationSuccessHandler(jwtTokenizer))
//                        .failureHandler(new OAuth2MemberAuthenticationFailureHandler())
//                        .userInfoEndpoint().userService(customOAuth2UserService)
//                );
//        return http.build();
//    }
//
////    @Bean
////    public ClientRegistrationRepository clientRegistrationRepository() {
////        // 아래의 var 키워드는 컴파일러가 알아서 타입을 추론하는 방식으로 작동된다.
////        var clientRegistration = clientRegistration();
////
////        return new InMemoryClientRegistrationRepository(clientRegistration);
////    }
////
////    private ClientRegistration clientRegistration() {
////        return CommonOAuth2Provider
////                .GOOGLE
////                .getBuilder("google")
////                .clientId(clientId)
////                .clientSecret(clientSecret)
////                .build();
////    }
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
////    }
//
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*")); // vs List.of() 차이 다시 정리하기!!
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
//        @Override
//        public void configure(HttpSecurity builder) throws Exception {
//            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);
//
//            builder.addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);
//        }
//    }
//}
