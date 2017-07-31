package com.taotao.rest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CartItem;
import com.taotao.rest.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_CART_KEY}")
	private String REDIS_CART_KEY;

	@Value("${REDIS_CART_TIME_EXPIRE}")
	private Integer REDIS_CART_TIME_EXPIRE;

	@Override
	public TaotaoResult addCartToRedis(Long userId, String cartOfJson) {
		
		try {

			String json = jedisClient.get(REDIS_CART_KEY + ":" + userId);
			System.out.println(this.getClass() + ":" + "...");
			List<CartItem> addedItems = JsonUtils.jsonToList(json, CartItem.class);
			List<CartItem> addingItems = JsonUtils.jsonToList(cartOfJson, CartItem.class);

			for (CartItem cartItem : addingItems) {

				if (addedItems.contains(cartItem)) {
					CartItem item = addedItems.get(addedItems.indexOf(cartItem));
					item.setNum(item.getNum() + cartItem.getNum());
				}

				else {
					addedItems.add(cartItem);
				}

			}
			
			cartOfJson = JsonUtils.objectToJson(addedItems);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {

			jedisClient.set(REDIS_CART_KEY + ":" + userId, cartOfJson);
			jedisClient.expire(REDIS_CART_KEY + ":" + userId, REDIS_CART_TIME_EXPIRE);

			return TaotaoResult.ok();

		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

	@Override
	public TaotaoResult getCartFromRedis(Long userId) {
		
		try {
			String json = jedisClient.get(REDIS_CART_KEY + ":" + userId);
			System.out.println(json);
			return TaotaoResult.ok(json);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}

}
