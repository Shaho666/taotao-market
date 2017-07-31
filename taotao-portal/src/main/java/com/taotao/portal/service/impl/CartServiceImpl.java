package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import com.taotao.portal.service.UserService;

/**
 * 购物车服务
 * <p>
 * Title: CartServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.itcast.com
 * </p>
 * 
 * @author 入云龙
 * @date 2015年8月27日上午9:32:21
 * @version 1.0
 */

@Service
public class CartServiceImpl implements CartService {

	// @Value("${SERVICE_BASE_URL}")
	// private String SERVICE_BASE_URL;
	//
	// @Value("${ITEM_BASE_URL}")
	// private String ITEM_BASE_URL;

	@Value("${CAT_COOKIE_EXPIRE}")
	private Integer CAT_COOKIE_EXPIRE;
	
	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;
	
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

			if (StringUtils.isNoneBlank(tbItem.getImage())) {
				String image = tbItem.getImage();
				String[] strings = image.split(",");
				cartItem.setImage(strings[0]);
			}

			list.add(cartItem);
		}

		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), CAT_COOKIE_EXPIRE, true);

		TbUser user = userService.getUserByToken(request, response);
		if(user != null) {
			Map<String, String> cartMapTable = new Hashtable<String, String>();
			cartMapTable.put(user.getId().toString(), JsonUtils.objectToJson(list));
		}
		
		return TaotaoResult.ok();
	}

	/**
	 * 取购物车信息
	 */
	public List<CartItem> getItemListFromCart(HttpServletRequest request, HttpServletResponse response) {
		// 从cookie中取商品列表
		
		TbUser user = userService.getUserByToken(request, response);
		System.out.println(this.getClass() + ":" + user);
		String cartString = CookieUtils.getCookieValue(request, "TT_CART", true);
		System.out.println(this.getClass() + ":" + cartString);
		if(cartString == null || cartString.isEmpty() || "".equals(cartString)) {
			String jsonList = HttpClientUtil.doGet("http://localhost:8081/rest/cart/redisCart/" + user.getId());
			//cartString = (String) TaotaoResult.format(jsonList).getData();
		}
		
		System.out.println(this.getClass() + ":" + cartString);
		try {
			List<CartItem> list = JsonUtils.jsonToList(cartString, CartItem.class);
			System.out.println(this.getClass() + ":" + list);
			if (list == null) {
				return new ArrayList<CartItem>();
			}
			return list;
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
