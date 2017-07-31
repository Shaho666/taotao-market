package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;

public interface CartService {

	TaotaoResult addCartToRedis(Long userId, String cartOfJson);
	TaotaoResult getCartFromRedis(Long userId);
	
}
