package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.rest.service.OrderService;

@Controller
@RequestMapping("/getOrder")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/orderList/{userId}")
	@ResponseBody
	public TaotaoResult getOrderList(@PathVariable Long userId) {
		
		try {
			TaotaoResult result = orderService.getOrderByUserId(userId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
	
	@RequestMapping("/orderItemList/{orderId}")
	@ResponseBody
	public TaotaoResult getOrderItemList(@PathVariable String orderId) {
		
		try {
			TaotaoResult result = orderService.getOrderItemListByOrderId(orderId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
	
	@RequestMapping("/orderShippingList/{orderId}")
	@ResponseBody
	public TaotaoResult getOrderShippingList(@PathVariable String orderId) {
		
		try {
			TaotaoResult result = orderService.getOrderShippingListByOrderId(orderId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
	
}
