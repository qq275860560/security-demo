package com.github.qq275860560.service;

import java.util.List;
import java.util.Set;

/**
 * @author jiangyuanlin@163.com
 *
 */

public abstract class OauthService {

	/**
	 * @param clientId
	 * @return
	 */
	public String getSecretByClientId(String clientId) {
		// 从缓存或数据库中查找
		return null;
	}

	/**
	 * @param clientId
	 * @return
	 */
	public Set<String> getRegisteredRedirectUriByClientId(String clientId) {
		// 从缓存或数据库中查找
		return null;

	}

	/**
	 * @param clientId
	 * @return
	 */
	public List<String> getScopeByClientId(String clientId) {
		// 从缓存或数据库中查找
		return null;
	}

	/**
	 * @param clientId
	 * @return
	 */
	public List<String> getAutoApproveScopesByClientId(String clientId) {
		// 从缓存或数据库中查找
		return null;
	}
	
 
	/**token的过期时间(单位为秒)
	 * @return
	 */
	public int getAccessTokenValiditySeconds() {
		return 10*365*24*3600;	 
	}
}
