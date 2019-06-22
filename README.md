[TOC]
[登录认证授权组件](https://github.com/qq275860560/security)调用示例

# 使用方式
## spring-boot项目pom文件引入依赖
```
<dependency>
 	<groupId>com.github.qq275860560</groupId>
	<artifactId>github-qq275860560-security</artifactId>
	<version>20190623</version>
</dependency>	
```
参考[pom.xml](https://github.com/qq275860560/security-demo/blob/master/pom.xml)

## 继承SecurityService抽象类重写方法
```

	/**根据登录账号查询密码
	  * 根据登录账号查询密码，此密码非明文密码，而是PasswordEncoder对明文加密后的密码，因为
     * spring security框架中数据库默认保存的是PasswordEncoder对明文加密后的密码
	 * 
	 * @param username 登录账号名称
	 * @return 返回字符串
	 */
	public String getPasswordByUserName(String username);

	/**
	 *   根据请求路径查询对应的角色名称列表，
	 *   登录用户至少拥有一个角色，才能访问
	 *   如果返回null或空集合或包含ROLE_ANONYMOUS，代表该url不需要权限控制，任何用户(包括匿名)用户都可以访问
	 *   如果url符合某个正则表达式，应当把正则表达式的角色也返回，比如/api/a的角色为ROLE_1,ROLE_2, 而数据库中还存在/api/*的角色为ROLE_3,ROLE_4；由于/api/a属于正则表达式/api/*,所以应当返回ROLE_1,ROLE_2,ROLE_3,ROLE_4
	 * @param url 请求路径（ip端口之后的路径）
	 * @return
	 */
	public Set<String> getRoleNameSetByUrI(String url); 

	/**
	 *   根据登录用户查询对应的角色名称列表，
	 *   如果返回null或空集合，代表该用户没有权限，这类用户其实跟匿名用户没有什么区别
	 *   如果username隶属于某高层次的角色或组织，应当把高层次的角色或组织对应的角色也返回，比如username的角色为ROLE_1, ROLE_1继承ROLE_2角色，并且username属于A部门，A部门拥有角色ROLE_3；所以应当返回ROLE_1,ROLE_2,ROLE_3
	 * @param username 登录用户名称
	 * @return
	 */
	public Set<String> getRoleNameSetByUsername(String username) ;
```
具体参考com.github.qq275860560.service.impl.SecurityServiceImpl
[接口说明](https://github.com/qq275860560/security/blob/master/src/main/java/com/github/qq275860560/service/SecurityService.java)
[实现代码示例](https://github.com/qq275860560/security-demo/blob/master/src/main/java/com/github/qq275860560/service/impl/SecurityServiceImpl.java)
## 编写业务代码
参考com.github.qq275860560.controller.UserController
[业务代码示例](https://github.com/qq275860560/security-demo/blob/master/src/main/java/com/github/qq275860560/controller/UserController.java)

## 运行
命令行切换到项目根目录下，执行
```
mvn spring-boot:run
```

此时，本地会默认开启8080端口

## 测试
### 登录功能
执行命令

```
curl -i -X POST   "http://localhost:8080/login?username=username1&password=123456"

```

### 登录成功响应结果

```
HTTP/1.1 200
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
Content-Type: application/json;charset=UTF-8
Content-Length: 375
Date: Thu, 30 May 2019 03:24:42 GMT

{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw","msg":"登录成功","code":200,"token_type":"bearer","expires_in":1874546682}


```

其中响应头部Authorization对应的值就是token，以后带着token就可以高高兴兴的访问系统了,只要有权限就能访问微服务


### 接口访问

```
curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/pageUser?pageNum=1&pageSize=10" 	   -H "Authorization:Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzQ1NDY2ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkODMzMDMyNi03MWRkLTRiNTgtOTk4Yi04OGJlNThlMmQxNTUifQ.Osw9GC9SuQQ3ESfqEFSLm0TJlsYXcTOrs5KtmZd72O91NcGSFDaoBl8R3m4DkOWjtH7syM67A8RbID-CiI43jw" 
```

### 接口认证授权成功响应结果

```
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 30 May 2019 03:25:38 GMT

{"msg":"分页搜索成功","code":200,"data":{"total":2,"pageList":[{"userId":"1","roleNames":"ROLE_ADMIN","username":"admin"},{"userId":"2","roleNames":"ROLE_ADMIN","username":"admin2"}]}}


```

### 批量执行(登录+请求)
```
token=`curl -i -X POST   "http://localhost:8080/login?username=username1&password=password1" | grep Authorization | cut -d' ' -f3` 
echo 当前令牌token为$token 
curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/pageUser?pageNum=1&pageSize=10" 	   -H "Authorization:Bearer $token"
	   
```
# 温馨提醒

* 此项目将会长期维护，增加或改进实用的功能
* 右上角点击star，给我继续前进的动力,谢谢