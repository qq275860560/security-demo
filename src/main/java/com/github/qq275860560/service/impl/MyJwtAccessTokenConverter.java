package com.github.qq275860560.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyJwtAccessTokenConverter extends JwtAccessTokenConverter {
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		setSigningKey("123");//

		/*String userName = authentication.getUserAuthentication().getName();
		User user = (User) authentication.getUserAuthentication().getPrincipal();//
		final Map<String, Object> additionalInformation = new HashMap<>();
		additionalInformation.put("userName", userName);
		additionalInformation.put("roles", user.getAuthorities());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);*/
		OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
		return enhancedToken;
	}

}
