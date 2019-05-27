package com.github.qq275860560.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;
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
 
	 

	 
	@Autowired
	public   MyJwtAccessTokenConverter(RsaSigner rsaSigner,RsaVerifier rsaVerifier) {
		super();
		this.setSigner(rsaSigner);// 如果只是资源服务器，可以不设置
		this.setVerifier(rsaVerifier);
	}
	

}
