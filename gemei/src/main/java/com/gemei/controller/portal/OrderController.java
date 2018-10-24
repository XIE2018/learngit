package com.gemei.controller.portal;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.gemei.common.Const;
import com.gemei.common.ResponseCode;
import com.gemei.common.ServerResponse;
import com.gemei.pojo.User;
import com.gemei.service.IOrderService;
import com.google.common.collect.Maps;


@Controller
@RequestMapping("/order/")
public class OrderController {
	
	private static  final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private IOrderService iOrderService;
	
	@RequestMapping("create.do")
	@ResponseBody
	public ServerResponse create(HttpSession session,Integer shippingId){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iOrderService.createOrder(user.getId(), shippingId);
	}
	
	@RequestMapping("cancel.do")
	@ResponseBody
	public ServerResponse cancel(HttpSession session,Long orderNo){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iOrderService.cancel(user.getId(), orderNo);
	}
	
	@RequestMapping("get_order_cart_product.do")
	@ResponseBody
	public ServerResponse getOrderCartProduct(HttpSession session){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iOrderService.getOrderCartProduct(user.getId());
				
	}
	@RequestMapping("order_detail.do")
	@ResponseBody
	public ServerResponse orderDetail(HttpSession session,Long orderNo){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iOrderService.getOrderDetail(user.getId(), orderNo);
				
	}
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse list(HttpSession session,@RequestParam(value="pageNum",defaultValue="1") int pageNum,@RequestParam(value="pageSize",defaultValue="10") int pageSize){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return iOrderService.getOrderList(user.getId(), pageNum, pageSize);		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("pay.do")
	@ResponseBody
	public ServerResponse pay(HttpSession session,Long orderNo,HttpServletRequest request){
		//HttpServletRequest把生成的二维码放到服务器上，把链接返回给前端用来扫码支付
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		String path = request.getSession().getServletContext().getRealPath("upload");
		return iOrderService.pay(orderNo, user.getId(), path);
	}
	
	@RequestMapping("alipay_callback.do")
	@ResponseBody
	public Object alipayCallback(HttpServletRequest request){
		Map<String,String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){

                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.
        
		params.remove("sign_type");
		boolean alipayRSACheckedV2;
		try {
			alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "uft-8", Configs.getSignType());
			if(!alipayRSACheckedV2){
				return ServerResponse.createByErrorMessage("非法请求");
			}
		} catch (AlipayApiException e) {
			
			logger.error("支付宝验证回调异常",e);
		}
		
		//
		ServerResponse serverResponse = iOrderService.aliCallback(params);
		if(serverResponse.isSuccess()){
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		}
		return Const.AlipayCallback.RESPONSE_FAILED;
	}
	/**
	 * 轮询是否付款成功
	 * @param session
	 * @param orderNo
	 * @param request
	 * @return
	 */
	@RequestMapping("query_order_pay_status.do")
	@ResponseBody
	public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session,Long orderNo){
		//HttpServletRequest把生成的二维码放到服务器上，把链接返回给前端用来扫码支付
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		
		ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
		if(serverResponse.isSuccess()){
			return ServerResponse.createBySuccess(true);
		}
		return ServerResponse.createBySuccess(false);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
