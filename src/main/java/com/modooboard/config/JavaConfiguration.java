package com.modooboard.config;

import com.modooboard.member.repository.MemberRepository;
import com.modooboard.member.service.JwtMemberService;
import com.modooboard.member.service.MemberService;
import com.modooboard.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class JavaConfiguration {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    @Bean
    public MemberService memberService() {
        return new JwtMemberService(memberRepository, passwordEncoder, authorityUtils);
    }
}
