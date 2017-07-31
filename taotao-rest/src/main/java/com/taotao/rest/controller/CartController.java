package com.taotao.rest.controller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.rest.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@RequestMapping(value = "/redis", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult addCartToRedis(@RequestParam Map<String, String> cartMap) {

		try {
			Set<Entry<String, String>> entrySet = cartMap.entrySet();

			for (Entry<String, String> entry : entrySet) {
				cartService.addCartToRedis(Long.parseLong(entry.getKey()), entry.getValue());
				break;
			}

			return TaotaoResult.ok();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	@RequestMapping("/redisCart/{userId}")
	@ResponseBody
	public TaotaoResult getCartFromRedis(@PathVariable Long userId) {
		try {
			TaotaoResult result = cartService.getCartFromRedis(userId);
			System.out.println(this.getClass() + ":" + userId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

}
