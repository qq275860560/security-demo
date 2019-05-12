[TOC]
[登录认证授权组件](https://github.com/qq275860560/security)调用示例

# 使用方式
## spring-boot项目pom文件引入依赖
```
<dependency>
 	<groupId>com.github.qq275860560</groupId>
	<artifactId>github-qq275860560-security</artifactId>
	<version>20190512</version>
</dependency>	
```
参考pom.xml

## 实现SecurityService接口
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
参考com.github.qq275860560.service.impl.SecurityServiceImpl

## 编写业务代码
参考com.github.qq275860560.controller.WebController

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
curl -i -H "Content-Type:application/json;charset=UTF-8" \
	  -X POST   http://localhost:8080/login \
	  -d '{"username":"username1","password":"password1"}'

```

### 登录成功响应结果

```
HTTP/1.1 200
Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTczNjcwMDl9.H1JcMvQQInYx8IM2iTkW-WQIN8eUQw-mBwRbbT0SBoXKeyXijNM3jvDpHLBisWNIobwPDIUqMIGEdk3MRC6TBAeTHxmZZacZbNenrsn9mB8qJU_P1zG7Hi6mRjrPBtdg0cEW44VGl6z_cUNgh1wjR7aMMhNzF7EQ0JxQkBl4P5g
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Content-Length: 45
Date: Wed, 08 May 2019 01:56:49 GMT

{"msg":"登录成功","code":200,"data":null}

```

响应头部Authorization对应的值就是token，以后带着token就可以高高兴兴的访问系统了,只要有权限就会访问


### 接口访问

```
curl -i -H "Content-Type:application/json;charset=UTF-8" \
	   -H "Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc3MjEwOTd9.JJi-w_Y2dXZRTqn65Lrpdn3aturDrJmbop-mH2NeAZ_SS6QiYTPuDs9rMkU7j7GL2qLpUScncWID4Ny1usaeGx2ZPo_6yMSUal3yPVOU5tnmITAgqYqh1fLBnc3wnTe9giqwJ2XEqAfS15DsNtqwEHblvhjfdMhGql0hUwOTXA7cwVRLitM5WxttpYUjhpAc8A7we2KXPZ-Kd9G5abzjj6bRW_Y3DwoA28nCfmmYWKZzSw4vFQlZP8STXuo4aBT6MNcqH29K5-jKaG_iEf_so1EQvgaAErl19LzRnHyGxNducEvmmtywP1W6tQAvGEmROfmTrNm1as74_gn9AsMbjg" \
	   -X POST http://localhost:8080/api/github/qq275860560/web/pageUser \
	   -d '{"pageNum":1,"pageSize":10}'
```

### 接口认证授权成功响应结果

```
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 11 May 2019 20:30:43 GMT

{"msg":"分页搜索成功","code":"OK","data":{"total":2,"list":[{"roles":"ROLE_ADMIN","userId":"1","username":"admin"},{"roles":"ROLE_ADMIN","userId":"2","username":"admin2"}]}}


```

# 温馨提醒

* 此项目将会长期维护，增加或改进实用的功能
* 右上角点击star，给我继续前进的动力,谢谢