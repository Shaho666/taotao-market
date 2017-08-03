package com.taotao.portal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.portal.pojo.OrderInfo;

public interface OrderService {

	//OrderCart getOrderCart(Long userId, HttpServletRequest request, HttpServletResponse response);
	String createOrder(OrderInfo orderInfo, HttpServletRequest request, HttpServletResponse response);
	List<TbOrder> getOrderList(Long userId);
	List<TbOrderItem> getOrderItemList(String orderId);
	List<TbOrderShipping> getOrderShipping(String orderId);
}
