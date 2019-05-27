package com.github.qq275860560.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.qq275860560.service.OauthService;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
public class OauthServiceImpl  extends OauthService {
	
	private Map<String, Map<String,Object>> cache = new HashMap<String, Map<String,Object>>() {
		{
			put("client1", new HashMap<String,Object>() {
				{					
					put("clientId","client1");
					put("secret",new BCryptPasswordEncoder().encode("secret1"));
					put("registeredRedirectUri","http://localhost:8081/oauth2/client1/getCode");
					put("scope","USER");//不需要前缀SCOPE_
					put("autoApproveScopes","USER");//不需要前缀SCOPE_
			 
				}
			});
			put("admin", new HashMap<String,Object>() {
				{
					put("clientId","admin");
					put("secret",new BCryptPasswordEncoder().encode("admin"));
					put("registeredRedirectUri","http://localhost:8081/oauth2/admin/getCode");
					put("scope","ADMIN,USER");//不需要前缀SCOPE_
					put("autoApproveScopes","ADMIN,USER");//不需要前缀SCOPE_
				}
			});
		}
	};
	
	/**
	 * @param clientId
	 * @return
	 */ 
	public String getSecretByClientId(String clientId) {
		// 从缓存或数据库中查找
		return (String)cache.get(clientId).get("secret");
	}
	
 
	/**
	 * @param clientId
	 * @return
	 */
	public Set<String> getRegisteredRedirectUriByClientId(String clientId) {
		// 从缓存或数据库中查找
		 String registeredRedirectUri=(String)cache.get(clientId).get("registeredRedirectUri");
		 return new HashSet<String>(Arrays.asList(registeredRedirectUri.split(",")));
		  
	}
 
	/**
	 * @param clientId
	 * @return
	 */
	public List<String> getScopeByClientId(String clientId) {
		// 从缓存或数据库中查找
		String scope = (String)cache.get(clientId).get("scope");
		return Arrays.asList(scope.split(","));
	}
	/**
	 * @param clientId
	 * @return
	 */
	public List<String> getAutoApproveScopesByClientId(String clientId) {
		// 从缓存或数据库中查找
		String autoApproveScopes = (String)cache.get(clientId).get("autoApproveScopes");
		return Arrays.asList(autoApproveScopes.split(","));
	}
	 
}
