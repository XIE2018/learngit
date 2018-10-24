package com.gemei.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gemei.common.Const;
import com.gemei.common.ResponseCode;
import com.gemei.common.ServerResponse;
import com.gemei.pojo.Product;
import com.gemei.pojo.User;
import com.gemei.service.IFileService;
import com.gemei.service.IProductService;
import com.gemei.service.IUserService;
import com.gemei.util.CookieUtil;
import com.gemei.util.JsonUtil;
import com.gemei.util.PropertiesUtil;
import com.gemei.util.RedisClusterPoolUtil;
import com.gemei.util.RedisPoolUtil;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private IProductService iProductService;
	
	@Autowired
	private IFileService iFileService;
	
	@RequestMapping("save.do")
	@ResponseBody
	public ServerResponse productSave(HttpServletRequest httpServletRequest,HttpSession session,Product product){
		//User user = (User)session.getAttribute(Const.CURRENT_USER);
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
			return iProductService.saveOrUpdateProduct(product);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	/**
	 * 商品上下架
	 * @param session
	 * @param product
	 * @return
	 */
	@RequestMapping("set_sale_status.do")
	@ResponseBody
	public ServerResponse setSaleStatus(HttpServletRequest httpServletRequest,HttpSession session,Integer productId,Integer status){
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
			return iProductService.setSaleStatus(productId, status);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse getDetail(HttpServletRequest httpServletRequest,HttpSession session,Integer productId){
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
			//填充业务
			return iProductService.manageProductDetail(productId);
			
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse getList(HttpServletRequest httpServletRequest,HttpSession session,@RequestParam(value="pageNum",defaultValue="1") int pageNum,@RequestParam(value="pageSize",defaultValue="10") int pageSize){
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
			//填充业务
			return iProductService.getProductList(pageNum, pageSize);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse productSearch(HttpServletRequest httpServletRequest,HttpSession session,String productName,Integer productId,@RequestParam(value="pageNum",defaultValue="1") int pageNum,@RequestParam(value="pageSize",defaultValue="10") int pageSize){
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
			//填充业务
			return iProductService.searchProduct(productName, productId, pageNum, pageSize);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	/**
	 * 图片文件上传
	 * @param session
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse upload(HttpServletRequest httpServletRequest,HttpSession session,@RequestParam(value="upload_file",required=false) MultipartFile file,HttpServletRequest request){
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
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
			
			Map fileMap = Maps.newHashMap();
			fileMap.put("uri", targetFileName);
			fileMap.put("url", url);
			return ServerResponse.createBySuccess(fileMap);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}	
	}
	/**
	 * 富文本上传
	 * @param session
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("richtext_img_upload.do")
	@ResponseBody
	public Map richtextImgUpload(HttpServletRequest httpServletRequest,HttpSession session,@RequestParam(value="upload_file",required=false) MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		Map resultMap = Maps.newHashMap();
		String loginToken = CookieUtil.readLoginToken(httpServletRequest);
		if(StringUtils.isEmpty(loginToken)){
			resultMap.put("success", false);
			resultMap.put("msg","请登录管理员");
			return resultMap;
		}
		String userJsonStr = RedisClusterPoolUtil.get(loginToken);
		User user = JsonUtil.string2Obj(userJsonStr, User.class);
		
		if(user == null){
			resultMap.put("success", false);
			resultMap.put("msg","请登录管理员");
			return resultMap;
		}
		
		if(iUserService.checkAdminRole(user).isSuccess()){
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			
			if(StringUtils.isBlank(targetFileName)){
				resultMap.put("success", false);
				resultMap.put("msg","上传失败");
				return resultMap;
			}
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
			resultMap.put("success", true);
			resultMap.put("msg","上传成功");
			resultMap.put("file_path",url);
			response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
			return resultMap;
		}else{
			resultMap.put("success", false);
			resultMap.put("msg","无权限操作");
			return resultMap;
		}	
	}
}
