package com.modooboard.security.oauth2.oauth2user;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getSocialId();

    public abstract String getDisplayName();

    public abstract String getProfileImage();
}
