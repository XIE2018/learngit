package com.gemei.controller.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gemei.common.Const;
import com.gemei.common.ResponseCode;
import com.gemei.common.ServerResponse;
import com.gemei.pojo.User;
import com.gemei.service.ICategoryService;
import com.gemei.service.IUserService;
import com.gemei.util.CookieUtil;
import com.gemei.util.JsonUtil;
import com.gemei.util.RedisClusterPoolUtil;
import com.gemei.util.RedisPoolUtil;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private ICategoryService iCategoryService;
	//如果没有串parentId,我们给他传一个默认的值
	
	@RequestMapping("add_category.do")
	@ResponseBody
	public ServerResponse addCategory(HttpServletRequest httpServletRequest,HttpSession session,String categoryName,@RequestParam(value="parentId",defaultValue="0") int parentId){
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//效验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iCategoryService.addCategory(categoryName, parentId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	}
	@RequestMapping("set_category_name.do")
	@ResponseBody
	public ServerResponse setCategoryName(HttpServletRequest httpServletRequest,HttpSession session,Integer categoryId,String categoryName){
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user ==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//效验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			
			return iCategoryService.updateCategoryName(categoryId, categoryName);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	}
	@RequestMapping("get_category.do")
	@ResponseBody
	public ServerResponse getChildrenParallelCategory(HttpServletRequest httpServletRequest,HttpSession session,@RequestParam(value="categoryId",defaultValue="0") int categoryId){
	
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//查询节点，并且不递归，保持平级
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iCategoryService.getChildrenParallelCategory(categoryId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	
	}
	
	@RequestMapping("get_deep_category.do")
	@ResponseBody
	public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest httpServletRequest,HttpSession session,@RequestParam(value="categoryId",defaultValue="0") int categoryId){
	
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//查询当前节点的id，和递归子节点的ID
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iCategoryService.selectCategoryAndChildrenById(categoryId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	
	}
}
