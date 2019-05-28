package com.github.qq275860560.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import com.github.qq275860560.service.OauthService;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
public class OauthServiceImpl  extends OauthService {
	
	private Map<String, Map<String,Object>> client_cache = new HashMap<String, Map<String,Object>>() {
		{
			put("client1", new HashMap<String,Object>() {
				{					
					put("secret",new BCryptPasswordEncoder().encode("secret1"));
					put("registeredRedirectUri","http://localhost:8081/oauth2/client1/getCode");
					put("scope","USER");//不需要前缀SCOPE_
					put("autoApproveScopes","USER");//不需要前缀SCOPE_
			 
				}
			});
			put("admin", new HashMap<String,Object>() {
				{
					put("secret",new BCryptPasswordEncoder().encode("admin"));
					put("registeredRedirectUri","http://localhost:8081/oauth2/admin/getCode");
					put("scope","ADMIN,USER");//不需要前缀SCOPE_
					put("autoApproveScopes","ADMIN,USER");//不需要前缀SCOPE_
				}
			});
			
			put("username1", new HashMap<String,Object>() {
				{
					put("scope","ADMIN,USER");			
				}
			});
		}
	};
	
	/**
	 * @param clientId
	 * @return
	 */
	@Override
	public String getSecretByClientId(String clientId) {
		// 从缓存或数据库中查找
		return (String)client_cache.get(clientId).get("secret");
	}
	
 
	/**
	 * @param clientId
	 * @return
	 */
	@Override
	public Set<String> getRegisteredRedirectUriByClientId(String clientId) {
		// 从缓存或数据库中查找
		 String registeredRedirectUri=(String)client_cache.get(clientId).get("registeredRedirectUri");
		 return new HashSet<String>(Arrays.asList(registeredRedirectUri.split(",")));
		  
	}
 
	/**
	 * @param clientId
	 * @return
	 */
	@Override
	public Set<String> getScopesByClientId(String clientId) {//不要SCOPE_开头，前端传过来也不要SCOPE_开头
		// 从缓存或数据库中查找
		String scope = (String)client_cache.get(clientId).get("scope");
		return new HashSet<>(Arrays.asList(scope.split(",")));
	}
	/**
	 * @param clientId
	 * @return
	 */
	@Override
	public Set<String> getAutoApproveScopesByClientId(String clientId) {//不要SCOPE_开头，前端传过来也不要SCOPE_开头
		// 从缓存或数据库中查找
		String autoApproveScopes = (String)client_cache.get(clientId).get("autoApproveScopes");
		return new HashSet<>(Arrays.asList(autoApproveScopes.split(",")));
	}
	 
	
	private Map<String, Map<String, Object>> url_cache = new HashMap<String, Map<String, Object>>() {
		{

			put("/oauth2/*", new HashMap<String, Object>() {
				{
					put("scopes", "SCOPE_USER");// 至少要此权限才能访问
				}
			});

			put("/oauth2/save", new HashMap<String, Object>() {
				{
					put("scopes", "SCOPE_ADMIN");// 至少要此权限才能访问
				}
			});
			put("/oauth2/get", new HashMap<String, Object>() {
				{
					put("scopes", "");
				}
			});

		}
	};
	
	/**
	 * @param url
	 * @return
	 */
	@Override
	public Set<String> getScopesByUrI(String url) {//SCOPE_开头
		// 从缓存或数据库中查找
		// 从缓存或数据库中查找
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		Set<String> set = new HashSet<>();
		for (String pattern : url_cache.keySet()) {
			if (antPathMatcher.match(pattern, url)) {
				Map<String, Object> map = (Map<String, Object>) url_cache.get(pattern);
				if (map == null)
					continue;
				String attributesString = (String) map.get("scopes");
				if (StringUtils.isEmpty(attributesString))
					continue;
				set.addAll(Arrays.asList(attributesString.split(",")));
			}
		}
		return set;
	}
 
}
