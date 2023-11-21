package com.modooboard.security.oauth2.dto;

import com.modooboard.member.entity.Member;
import com.modooboard.security.oauth2.oauth2user.GoogleOAuth2UserInfo;
import com.modooboard.security.oauth2.oauth2user.KakaoOAuth2UserInfo;
import com.modooboard.security.oauth2.oauth2user.NaverOAuth2UserInfo;
import com.modooboard.security.oauth2.oauth2user.OAuth2UserInfo;
import com.modooboard.security.utils.CustomAuthorityUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes { // 소셜별로 데이터를 받는 데이터 분기처리를 하는 DTO 클래스
    private String nameAttributeKey;
    private OAuth2UserInfo oauth2UserInfo;
    private CustomAuthorityUtils authorityUtils;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo, CustomAuthorityUtils authorityUtils) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
        this.authorityUtils = authorityUtils;
    }

    public static OAuthAttributes of(Member.SocialType socialType,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes,
                                     CustomAuthorityUtils authorityUtils) {

        if (socialType == Member.SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes, authorityUtils);
        }
        if (socialType == Member.SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes, authorityUtils);
        }
        return ofGoogle(userNameAttributeName, attributes, authorityUtils);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes, CustomAuthorityUtils authorityUtils) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .authorityUtils(authorityUtils)
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, CustomAuthorityUtils authorityUtils) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .authorityUtils(authorityUtils)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes, CustomAuthorityUtils authorityUtils) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .authorityUtils(authorityUtils)
                .build();
    }

    public Member toEntity(Member.SocialType socialType) {
        String email = UUID.randomUUID() + "@socialUser.com";
        List<String> roles = authorityUtils.createRoles(email);

        return Member.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getSocialId())
                .email(email)
                .displayName(oauth2UserInfo.getDisplayName())
                .profileImage(oauth2UserInfo.getProfileImage())
                .roles(roles)
                .build();
    }
}
