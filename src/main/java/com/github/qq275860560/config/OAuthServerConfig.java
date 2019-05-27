package com.github.qq275860560.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.github.qq275860560.security.MyUserDetailsService;
import com.github.qq275860560.service.impl.MyAuthorizationCodeServices;
import com.github.qq275860560.service.impl.MyClientDetailsService;

@Configuration
public class OAuthServerConfig {

	@Configuration
	@EnableResourceServer()
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.requestMatchers()
					// 防止被主过滤器链路拦截
					.antMatchers("/oauth2/**").and().authorizeRequests().antMatchers("/oauth2/save")
					.access("#oauth2.hasScope('SCOPE_ADMIN')").and().authorizeRequests().antMatchers("/oauth2/get")
					.access("#oauth2.hasScope('SCOPE_USER')");

		}

	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Autowired
		private MyUserDetailsService myUserDetailsService;

		/**
		
		//传统
		curl -i -X GET "http://localhost:8080/login?username=username1&password=password1"
		curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/pageUser?pageNum=1&pageSize=10" -H "Authorization:Bearer  eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzM4MTc4ODN9.ALDkjrDct9yazycSBTsoHeig2oZPa0zvLMtUWM2pg_FxubYmAxhXJDWSqxFBVybhHuszETKFJPOl1nj4YxkajQ" 
		
		curl -i -X GET "http://localhost:8080/login?username=admin&password=admin"
		curl -i -X GET "http://localhost:8080/api/github/qq275860560/user/saveUser?username=username2" -H "Authorization:Bearer  eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTg3NDE4ODM3OX0.SM_e5L1E42fZVnACDBvEjyYbcPTYej9jG8747NHoNgAi5d01Y41cHTwI58awEtygQFolV4rd5wF_mPsgVAt5PA" 
		
		//oauth2客户端模式
		token=`curl -i -X POST "http://client1:secret1@localhost:8080/oauth/token?grant_type=client_credentials"  | grep access_token | awk -F "\"" '{print $4}'`
		echo 当前token为$token
		curl -i -X POST "http://localhost:8080/oauth2/get?access_token=$token"
		
		
		
		//oauth2密码模式
		token=`curl -i -X POST "http://client1:secret1@localhost:8080/oauth/token?grant_type=password&username=username1&password=password1"  | grep access_token | awk -F "\"" '{print $4}'`
		echo 当前token为$token
		curl -i -X POST "http://localhost:8080/oauth2/get?access_token=$token"
		
		
		//oauth2认证码模式     
		code=`curl -i -X GET "http://localhost:8080/oauth/authorize?client_id=client1&response_type=code"  -H "Authorization:Bearer  eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQyMDIzOTB9.IuFt9RhFSjhVKeIVevSq30QGC84d6Uc52UbEwY8-IVPUSf6yoZ9Xd6PFMEq2o_mw1hmUpATJS53_m7hYgokCQw"  | grep Location | cut -d'=' -f2` 
		echo 当前认证码为$code
		token=`curl -i -X POST "http://localhost:8080/oauth/token?grant_type=authorization_code&client_id=client1&client_secret=secret1&scope=SCOPE_USER&code=$code"  | grep access_token | awk -F "\"" '{print $4}'`
		echo 当前token为$token
		curl -i -X POST "http://localhost:8080/oauth2/get?access_token=$token"
		
		
		//oauth2刷新token
		code=`curl -i -X GET "http://localhost:8080/oauth/authorize?client_id=client1&response_type=code"  -H "Authorization:Bearer  eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQyMDIzOTB9.IuFt9RhFSjhVKeIVevSq30QGC84d6Uc52UbEwY8-IVPUSf6yoZ9Xd6PFMEq2o_mw1hmUpATJS53_m7hYgokCQw"  | grep Location | cut -d'=' -f2` 
		echo 当前认证码为$code
		refresh_token=`curl -i -X POST "http://localhost:8080/oauth/token?grant_type=authorization_code&client_id=client1&client_secret=secret1&scope=SCOPE_USER&code=$code"  | grep refresh_token | awk -F "\"" '{print $12}'`
		echo 当前refresh_token为$refresh_token
		token=`curl -i -X POST "http://localhost:8080/oauth/token?grant_type=refresh_token&client_id=client1&client_secret=secret1&refresh_token=${refresh_token}"  | grep access_token | awk -F "\"" '{print $4}'`
		echo 当前token为$token
		curl -i -X POST "http://localhost:8080/oauth2/get?access_token=$token"                    
		
		//oauth2校验token
		token=`curl -i -X POST "http://client1:secret1@localhost:8080/oauth/token?grant_type=client_credentials"  | grep access_token | awk -F "\"" '{print $4}'`
		echo 当前token为$token
		curl -i -X POST  "http://client1:secret1@localhost:8080/oauth/check_token?token=$token"                   
		            
		 */

		@Autowired
		private MyClientDetailsService myClientDetailsService;

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.withClientDetails(myClientDetailsService);
		}

		 

		@Bean
		public TokenStore tokenStore() {
			return new InMemoryTokenStore();
			// 需要使用 redis 的话，放开这里
			// return new RedisTokenStore(redisConnectionFactory);
		}

		@Autowired
		private MyAuthorizationCodeServices myAuthorizationCodeServices;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
			endpoints.tokenStore(tokenStore());
			endpoints.authenticationManager(authenticationManager);
			endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
			endpoints.userDetailsService(myUserDetailsService);
			endpoints.reuseRefreshTokens(true);
			endpoints.authorizationCodeServices(myAuthorizationCodeServices);
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
			oauthServer.allowFormAuthenticationForClients();// /oauth/confirm_access中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter
			oauthServer.tokenKeyAccess("permitAll()"); // url:/oauth/token_key,exposes public key for token verification
														// if using JWT tokens
			oauthServer.checkTokenAccess("isAuthenticated()"); // url:/oauth/check_token allow check token
		}
	}

}
