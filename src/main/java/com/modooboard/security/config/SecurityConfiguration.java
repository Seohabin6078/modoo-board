package com.modooboard.security.config;

import com.modooboard.member.service.MemberService;
import com.modooboard.security.filter.JwtAuthenticationFilter;
import com.modooboard.security.filter.JwtVerificationFilter;
import com.modooboard.security.handler.MemberAccessDeniedHandler;
import com.modooboard.security.handler.MemberAuthenticationEntryPoint;
import com.modooboard.security.handler.MemberAuthenticationFailureHandler;
import com.modooboard.security.handler.MemberAuthenticationSuccessHandler;
import com.modooboard.security.jwt.JwtTokenizer;
import com.modooboard.security.oauth2.handler.OAuth2MemberAuthenticationFailureHandler;
import com.modooboard.security.oauth2.handler.OAuth2MemberAuthenticationSuccessHandler;
import com.modooboard.security.oauth2.oauth2user.CustomOAuth2UserService;
import com.modooboard.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .headers().frameOptions().sameOrigin() // H2 웹 콘솔을 정상적으로 사용하기 위한 설정
//                .and()
//                .csrf().disable()
//                .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/process_login")
//                .failureUrl("/login?error")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/")
//                .and()
//                .exceptionHandling().accessDeniedPage("/access-denied") // 403에러 발생시 리다이렉트
//                .and()
//                .authorizeHttpRequests(authorize -> authorize
////                        .antMatchers("/members/**").hasRole("USER") >> "/members" 및 그 하위 모든 uri와 매칭된다.
//                        .antMatchers("/**").permitAll()
//                );

        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(withDefaults()) // withDefaults()를 적용하면 corsConfigurationSource라는 이름으로 등록된 Bean을 이용한다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint()) // 여긴 왜 component 붙였는데 new로 생성하였는가?
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/members").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/members/**").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/members/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/members/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2MemberAuthenticationSuccessHandler(jwtTokenizer))
                        .failureHandler(new OAuth2MemberAuthenticationFailureHandler())
                        .userInfoEndpoint().userService(oAuth2UserService));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // vs List.of() 차이 다시 정리하기!!
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/login"); // "/login"이 디폴트라 생략가능
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler()); // 일반적으로 인증을 위한 Security Filter마다 handler 구현 클래스를 각각 생성하기 때문에 new 키워드를 사용해도 무방하다.
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            // todo 아래와 같이 설정하면 JwtAuthenticationFilter와 OAuth2LoginAuthenticationFilter 각각 호출뒤에 JwtVerificationFilter가 호출되는지 테스트해보기!!
            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class)
                    .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);
        }
    }
}
