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
import com.gemei.service.IOrderService;
import com.gemei.service.IUserService;
import com.gemei.util.CookieUtil;
import com.gemei.util.JsonUtil;
import com.gemei.util.RedisClusterPoolUtil;
import com.gemei.util.RedisPoolUtil;
import com.gemei.vo.OrderVo;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private IOrderService iOrderService;
	
	@RequestMapping("order_list.do")
	@ResponseBody
	public ServerResponse<PageInfo> orderList(HttpServletRequest httpServletRequest,HttpSession session,@RequestParam(value="pageNum",defaultValue="1") int pageNum,
			@RequestParam(value="pageSize",defaultValue="10") int pageSize){
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录");
		}
		
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iOrderService.manageList(pageNum, pageSize);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	
	@RequestMapping("order_detail.do")
	@ResponseBody
	public ServerResponse<OrderVo> orderDetail(HttpServletRequest httpServletRequest,HttpSession session,Long orderNo){
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iOrderService.manageDetail(orderNo);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	
	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse<PageInfo> orderSearch(HttpServletRequest httpServletRequest,HttpSession session,Long orderNo,@RequestParam(value="pageNum",defaultValue="1") int pageNum,
			@RequestParam(value="pageSize",defaultValue="10") int pageSize){
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iOrderService.manageSearch(orderNo, pageNum, pageSize);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	
	@RequestMapping("send_goods.do")
	@ResponseBody
	public ServerResponse<String> orderSendGoods(HttpServletRequest httpServletRequest,HttpSession session,Long orderNo){
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			
			return iOrderService.manageSendGoods(orderNo);
					
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
}
