package com.modooboard.security.oauth2.oauth2user;

import com.modooboard.member.entity.Member;
import com.modooboard.member.repository.MemberRepository;
import com.modooboard.security.oauth2.dto.OAuthAttributes;
import com.modooboard.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Member.SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes, authorityUtils);
        Member member = findOrCreateMember(oAuthAttributes, socialType);
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(member.getRoles());

        return new CustomOAuth2User(authorities,
                attributes,
                oAuthAttributes.getNameAttributeKey(),
                member.getEmail(),
                member.getRoles());
    }

    private Member.SocialType getSocialType(String registrationId) {
        if (NAVER.equals(registrationId)) {
            return Member.SocialType.NAVER;
        }
        if (KAKAO.equals(registrationId)) {
            return Member.SocialType.KAKAO;
        }
        return Member.SocialType.GOOGLE;
    }

    private Member findOrCreateMember(OAuthAttributes attributes, Member.SocialType socialType) {
        String socialId = attributes.getOauth2UserInfo().getSocialId();

        Optional<Member> optionalMember = memberRepository.findBySocialTypeAndSocialId(socialType, socialId);

        if (optionalMember.isEmpty()) {
            Member createdMember = attributes.toEntity(socialType);
            return memberRepository.save(createdMember);
        }

        return optionalMember.get();
    }
}
