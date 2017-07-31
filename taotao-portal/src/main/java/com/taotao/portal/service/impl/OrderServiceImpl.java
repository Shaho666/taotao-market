package com.taotao.portal.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.portal.pojo.OrderInfo;
import com.taotao.portal.service.OrderService;

/**
 * 订单管理
 * <p>Title: OrderServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.com</p> 
 * @author	入云龙
 * @date	2015年8月27日下午4:31:15
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	/**http://localhost:8085*/
	@Value("${ORDER_BASE_URL}")
	private String ORDER_BASE_URL;
	
	/**order/create*/
	@Value("${ORDER_CREATE_URL}")
	private String ORDER_CREATE_URL;
	
//	
//	@Override
//	public OrderCart getOrderCart(Long userId, HttpServletRequest request, HttpServletResponse response) {
//		//根据id查询用户的配送地址列表（未实现）
//		//从cookie中取商品列表
//		List<Item> list = cartServiceImpl.getItemListFromCart(request, response);
//		OrderCart orderCart = new OrderCart();
//		orderCart.setItemList(list);
//		return orderCart;
//	}
	@Override
	public String createOrder(OrderInfo orderInfo, HttpServletRequest request, HttpServletResponse response) {
		//先把orderInfo转换成json数据
		String json = JsonUtils.objectToJson(orderInfo);
		//调用订单系统的服务
		String string = HttpClientUtil.doPostJson(ORDER_BASE_URL + ORDER_CREATE_URL, json);
		//把string转换成TaotaoResult对象
		TaotaoResult result = TaotaoResult.format(string);
		System.out.println(this.getClass() + ":" + result);
		System.out.println(this.getClass() + ":" + result.getData());
		String orderId = result.getData().toString();
		
		CookieUtils.deleteCookie(request, response, "TT_CART");
		
		return orderId;
	}

}
