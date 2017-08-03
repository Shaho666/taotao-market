package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;

public interface OrderService {

	TaotaoResult getOrderByUserId(Long userId);
	TaotaoResult getOrderItemListByOrderId(String orderId);
	TaotaoResult getOrderShippingListByOrderId(String orderId);
	
}
