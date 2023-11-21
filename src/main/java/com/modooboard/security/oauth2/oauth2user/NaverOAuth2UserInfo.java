package com.modooboard.security.oauth2.oauth2user;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getSocialId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        // 과연 여기가 null 일 수 있나?? 애초에 null 이라는 거는 소셜 로그인 인증과정이 실패했다는 의미 같은데
        // 실패했다면 여기까지 오지못하고 exception이 터지지 않았을까
        // 그렇다면 여기 부분은 굳이 예외처리를 해줘야하나??
        // 예외처리 해줄 필요 없을 듯
        if (response == null) {
            return null;
        }
        return (String) response.get("id");
    }

    @Override
    public String getDisplayName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }
        return (String) response.get("nickname");
    }

    @Override
    public String getProfileImage() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }
        return (String) response.get("profile_image");
    }
}
