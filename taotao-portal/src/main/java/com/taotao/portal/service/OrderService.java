package com.taotao.portal.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.portal.pojo.OrderInfo;

public interface OrderService {

	//OrderCart getOrderCart(Long userId, HttpServletRequest request, HttpServletResponse response);
	String createOrder(OrderInfo orderInfo, HttpServletRequest request, HttpServletResponse response);
}
