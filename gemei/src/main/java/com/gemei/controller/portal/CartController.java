package com.gemei.controller.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gemei.common.Const;
import com.gemei.common.ResponseCode;
import com.gemei.common.ServerResponse;
import com.gemei.pojo.User;
import com.gemei.service.ICartService;
import com.gemei.util.CookieUtil;
import com.gemei.util.JsonUtil;
import com.gemei.util.RedisClusterPoolUtil;
import com.gemei.util.RedisPoolUtil;
import com.gemei.vo.CartVo;

@Controller
@RequestMapping("/cart/")
public class CartController {
		
	@Autowired
	private ICartService iCartService;
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<CartVo> list(HttpServletRequest httpServletRequest,HttpSession session){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "为登录需要强制登录");
		}
		return iCartService.list(user.getId());
	}
	
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse<CartVo> add(HttpServletRequest httpServletRequest,HttpSession session,Integer count,Integer productId){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "为登录需要强制登录");
		}
		
		return iCartService.add(user.getId(), productId, count);
	}
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse<CartVo> update(HttpServletRequest httpServletRequest,HttpSession session,Integer count,Integer productId){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "为登录需要强制登录");
		}
		
		return iCartService.update(user.getId(), productId, count);
	}
	
	@RequestMapping("delete_product.do")
	@ResponseBody
	public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest,HttpSession session,String productIds){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "为登录需要强制登录");
		}
		
		return iCartService.deleteProduct(user.getId(), productIds);
	}
	/**
	 * 全选
	 */
	@RequestMapping("select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest,HttpSession session){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "为登录需要强制登录");
		}
		
		return iCartService.selectOrUnSelect(user.getId(),null, Const.Cart.CHECKED);
	}
	/**
	 * 全反选
	 */
	@RequestMapping("un_select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest,HttpSession session){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "为登录需要强制登录");
		}
		
		return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
	}
	
	/**
	 * 单独选
	 */
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest,HttpSession session,Integer productId){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "为登录需要强制登录");
		}
		
		return iCartService.selectOrUnSelect(user.getId(),productId, Const.Cart.CHECKED);
	}
	/**
	 * 单独反选
	 */
	@RequestMapping("un_select.do")
	@ResponseBody
	public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest,HttpSession session,Integer productId){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "为登录需要强制登录");
		}
		
		return iCartService.selectOrUnSelect(user.getId(),productId, Const.Cart.UN_CHECKED);
	}
	//查询当前购物车里的数量
	@RequestMapping("get_cart_product_count.do")
	@ResponseBody
	public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest,HttpSession session){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			return ServerResponse.createBySuccess(0);
		}
		
		return iCartService.getCartProductCount(user.getId());
	}
}
