package com.github.qq275860560.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.qq275860560.respository.UserRespository;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Controller
@ResponseBody
@Slf4j
public class UserController {



	@Autowired
	private UserRespository userRespository;

	/*  curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/pageUser?pageNum=1&pageSize=10" -H "Authorization:Bearer  eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzM4MTc4ODN9.ALDkjrDct9yazycSBTsoHeig2oZPa0zvLMtUWM2pg_FxubYmAxhXJDWSqxFBVybhHuszETKFJPOl1nj4YxkajQ" 
	 *  curl -i -X POST "http://username1:password1@localhost:8080/api/github/qq275860560/user/pageUser?pageNum=1&pageSize=10" 
	*/
	@ApiOperation(value="获取用户列表", notes="获取用户列表")
	@RequestMapping(value = "/api/github/qq275860560/user/pageUser")
	public Map<String, Object> pageUser(@RequestParam(required=false) Integer pageNum,@RequestParam(required=false) Integer pageSize)  throws Exception{
		String currentLoginUsername=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + currentLoginUsername);
		Map<String, Object> data = userRespository.pageUser();
		return new HashMap<String, Object>() {
			{
				 
				put("code", HttpStatus.OK);//此字段可以省略，这里仿照蚂蚁金服的接口返回字段code，增加状态码说明
				put("msg", "分页搜索成功");//此字段可以省略，这里仿照蚂蚁金服的接口返回字段msg，增加说明
				put("data", data);								
			}
		};
	}

	
	/* curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/listUser" -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzMyNDk3Njl9.ELXDySOUIE1oq1OuRG0GHh7sUIFYxbr92Mlpp6RgOMWpTIxhpxV5_0qrI52BtsabDCtAst611KXqYZckGOBRAg" 	 
	*/
	@ApiOperation(value="获取用户列表", notes="获取用户列表")
	@RequestMapping(value = "/api/github/qq275860560/user/listUser")
	public Map<String, Object> listUser(@RequestParam(required=false) String username )  throws Exception{
		String currentLoginUsername=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + currentLoginUsername);
		List<Map<String, Object>> data = userRespository.listUser();
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "获取列表成功");
				put("data", data);
			}
		};
	}
	
	/*  curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/getUser?id=1" -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzMyNDk3Njl9.ELXDySOUIE1oq1OuRG0GHh7sUIFYxbr92Mlpp6RgOMWpTIxhpxV5_0qrI52BtsabDCtAst611KXqYZckGOBRAg"
	 *  
	 */
	@ApiOperation(value="获取用户", notes="根据id获取用户")
	@RequestMapping(value = "/api/github/qq275860560/user/getUser")
	public Map<String, Object> getUser(@RequestParam(required=true) String id)  throws Exception{
		String currentLoginUsername=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + currentLoginUsername);
		Map<String, Object> data=userRespository.getUser("");
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "获取对象成功");
				put("data", data);
			}
		};
	}
	
	
	/* curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/saveUser?username=admin2" -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzMyNDk3Njl9.ELXDySOUIE1oq1OuRG0GHh7sUIFYxbr92Mlpp6RgOMWpTIxhpxV5_0qrI52BtsabDCtAst611KXqYZckGOBRAg"
	   
	*/
	@ApiOperation(value="新增用户", notes="新增用户") 
	@RequestMapping(value = "/api/github/qq275860560/user/saveUser")
	public Map<String, Object> saveUser(@RequestParam(required=true) String username)  throws Exception{
		String currentLoginUsername=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + currentLoginUsername);
		userRespository.saveUser(new HashMap<String,Object>() {{put("username",username);}});
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}
	
	
	/* curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/updateUser?username=admin2"  -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzMyNDk3Njl9.ELXDySOUIE1oq1OuRG0GHh7sUIFYxbr92Mlpp6RgOMWpTIxhpxV5_0qrI52BtsabDCtAst611KXqYZckGOBRAg" 
	   
	*/
	@ApiOperation(value="更新用户", notes="更新用户") 
	@RequestMapping(value = "/api/github/qq275860560/user/updateUser")
	public Map<String, Object> updateUser(
			@RequestParam(required=true) String username)  throws Exception{
		String currentLoginUsername=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + currentLoginUsername);
		userRespository.updateUser(new HashMap<String,Object>() {{put("username",username);}});
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}
	
	/* curl -i -X POST "http://localhost:8080/api/github/qq275860560/user/deleteUser?id=1" -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE4NzMyNDk3Njl9.ELXDySOUIE1oq1OuRG0GHh7sUIFYxbr92Mlpp6RgOMWpTIxhpxV5_0qrI52BtsabDCtAst611KXqYZckGOBRAg" 
	   curl -i -X POST "http://username1:password1@localhost:8080/api/github/qq275860560/user/deleteUser?id=1"
	*/
	@ApiOperation(value="删除用户", notes="根据id删除用户")
	@RequestMapping(value = "/api/github/qq275860560/user/deleteUser")
	public Map<String, Object> deleteUser(
			@RequestParam(required=true) String id)  throws Exception{
		String currentLoginUsername=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + currentLoginUsername);
		userRespository.deleteUser(id);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	
	
	
}
