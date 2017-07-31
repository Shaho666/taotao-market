package com.taotao.sso.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.LogoutService;

@Service
public class LogoutServiceImpl implements LogoutService {

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;
	
	@Override
	public TaotaoResult logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		try {
			
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			CookieUtils.deleteCookie(request, response, "TT_TOKEN");
			jedisClient.del(REDIS_SESSION_KEY + ":" + token);
			
			session.invalidate();
			
			return TaotaoResult.ok();
			
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}

}
