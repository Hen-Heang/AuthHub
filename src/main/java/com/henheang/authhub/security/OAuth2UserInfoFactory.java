package com.henheang.authhub.security;

import com.henheang.authhub.domain.AuthProvider;
import com.henheang.authhub.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
//        if(registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
//            return new GoogleOAuth2UserInfo(attributes;
//        } else {
//            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
//        }
        return null;
    }
}
