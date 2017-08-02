package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.ItemService;
/**
 * 购物车服务
 */

@Service
public class CartServiceImpl implements CartService {

	/* /cart/redisCart/ */
	@Value("${REDIS_CART_URL}")
	private String REDIS_CART_URL;

	/* http://localhost:8081/rest */
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;

	/* 432000 */
	@Value("${CAT_COOKIE_EXPIRE}")
	private Integer CAT_COOKIE_EXPIRE;

	/* REDIS_SESSION */
	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;

	/* /cart/redis/ */
	@Value("${REDIS_CART_ITEM_ADD}")
	private String REDIS_CART_ITEM_ADD;
	
	@Value("${REDIS_CART_ITEM_DELETE}")
	private String REDIS_CART_ITEM_DELETE;

	@Autowired
	private ItemService itemService;

	@Override
	public TaotaoResult addCartItem(long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {

		// 1、接收controller传递过来的参数：商品id
		// 从cookie中取购物车商品列表
		List<CartItem> list = getItemListFromCart(request, response);
		boolean haveFlag = false;

		for (CartItem item : list) {
			if (item.getId().longValue() == itemId) {
				haveFlag = true;
				item.setNum(item.getNum() + num);
				item.setDate(new Date());
				break;
			}
		}
		// 判断是否存在此商品
		if (!haveFlag) {

			TbItem tbItem = itemService.getItemById(itemId);

			CartItem cartItem = new CartItem();
			cartItem.setId(itemId);
			cartItem.setNum(num);
			cartItem.setTitle(tbItem.getTitle());
			cartItem.setPrice(tbItem.getPrice());
			cartItem.setDate(new Date());

			if (StringUtils.isNoneBlank(tbItem.getImage())) {
				String image = tbItem.getImage();
				String[] strings = image.split(",");
				cartItem.setImage(strings[0]);
			}

			list.add(cartItem);
		}

		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), CAT_COOKIE_EXPIRE, true);

		return TaotaoResult.ok();
	}

	public List<CartItem> getCartFromRedis(TbUser user) {

		String cartString = null;

		if (user != null) {
			String jsonList = HttpClientUtil.doGet(REST_BASE_URL + REDIS_CART_URL + user.getId());
			cartString = (String) TaotaoResult.format(jsonList).getData();
		}

		return JsonUtils.jsonToList(cartString, CartItem.class);
	}

	/**
	 * 取购物车信息
	 */
	public List<CartItem> getItemListFromCart(HttpServletRequest request, HttpServletResponse response) {
		// 从cookie中取商品列表

		List<CartItem> cartItems = new ArrayList<CartItem>();

		//TbUser user = userService.getUserByToken(request, response);
		String cartString = CookieUtils.getCookieValue(request, "TT_CART", true);

		if (cartString != null) {
			cartItems.addAll(JsonUtils.jsonToList(cartString, CartItem.class));
		}

		try {

			return cartItems;
		} catch (Exception e) {
			return new ArrayList<CartItem>();
		}
	}

	/**
	 * 取购物车商品列表
	 * <p>
	 * Title: getCatItemList
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param request
	 * @return
	 * @see com.taotao.portal.service.CartService#getCatItemList(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public List<CartItem> getCatItemList(HttpServletRequest request) {
		// 从cookie中取商品列表
		try {
			List<CartItem> list = getItemListFromCart(request, null);
			return list;
		} catch (Exception e) {
			return new ArrayList<CartItem>();
		}
	}

	/**
	 * 更新购物车商品数量
	 * <p>
	 * Title: updateItemNum
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param itemId
	 * @param num
	 * @return
	 * @see com.taotao.portal.service.CartService#updateItemNum(long, int)
	 */
	@Override
	public TaotaoResult updateItemNum(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
		// 从cookie中把商品列表取出来
		List<CartItem> list = getItemListFromCart(request, response);
		// 遍历列表找商品
		for (CartItem cartItem : list) {
			if (cartItem.getId().longValue() == itemId) {
				// 更新商品数量
				cartItem.setNum(num);
				break;
			}
		}
		// 把购物车商品列表写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), CAT_COOKIE_EXPIRE, true);

		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {

		List<CartItem> itemList = getItemListFromCart(request, response);

		for (CartItem cartItem : itemList) {
			if (cartItem.getId() == itemId) {

				itemList.remove(cartItem);
				break;
			}
		}

		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), CAT_COOKIE_EXPIRE, true);

		return TaotaoResult.ok();
	}

}
