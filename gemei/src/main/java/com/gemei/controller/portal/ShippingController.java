package com.gemei.controller.portal;

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
import com.gemei.dao.ShippingMapper;
import com.gemei.pojo.Shipping;
import com.gemei.pojo.User;
import com.gemei.service.IShippingService;
import com.gemei.util.CookieUtil;
import com.gemei.util.JsonUtil;
import com.gemei.util.RedisClusterPoolUtil;
import com.gemei.util.RedisPoolUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {
	
	@Autowired
	private IShippingService iShippingService;
	
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse add(HttpServletRequest httpServletRequest,HttpSession session,Shipping shipping){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iShippingService.add(user.getId(), shipping);
	}
	/**
	 * 删除接口
	 */
	@RequestMapping("del.do")
	@ResponseBody
	public ServerResponse del(HttpServletRequest httpServletRequest,HttpSession session,Integer shippingId){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iShippingService.del(user.getId(), shippingId);
	}
	
	/**
	 * 更新
	 */
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse update(HttpServletRequest httpServletRequest,HttpSession session,Shipping shipping){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iShippingService.update(user.getId(), shipping);
	}
	/**
	 * 查询
	 */
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse<Shipping> select(HttpServletRequest httpServletRequest,HttpSession session,Integer shippingId){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iShippingService.select(user.getId(), shippingId);
	}
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> List(@RequestParam(value = "pageNum",defaultValue="1") int pageNum,
										@RequestParam(value = "pageSize",defaultValue="10") int pageSize,
										HttpSession session,HttpServletRequest httpServletRequest){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iShippingService.list(user.getId(), pageNum, pageSize);
	}
	
}
