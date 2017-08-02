package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;

public class OrderInterceptor implements HandlerInterceptor {

	@Autowired
	private UserService userService;

	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		TbUser user = userService.getUserByToken(request, response);

		if (user == null) {
			response.sendRedirect(SSO_LOGIN_URL + "?redirectURL=" + request.getRequestURL());
			return false;
		}

		request.setAttribute("user", user);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		/*
		 * TbUser user = (TbUser) request.getAttribute("user");
		 * 
		 * String string = CookieUtils.getCookieValue(request, "TT_CART", true);
		 * 
		 * Map<String, String> cartMapTable = new Hashtable<String, String>();
		 * cartMapTable.put(user.getId().toString(), string);
		 * 
		 * System.out.println(string);
		 * HttpClientUtil.doPost("http://localhost:8081/rest/cart/redis", cartMapTable);
		 */

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
