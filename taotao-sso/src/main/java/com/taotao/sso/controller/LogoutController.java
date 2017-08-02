package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.sso.service.LogoutService;

@Controller
public class LogoutController {

	@Autowired
	private LogoutService logoutService;
	
	@Value("${HOME_PAGE_URL}")
	private String HOME_PAGE_URL;
	
	@RequestMapping("/user/logout")
	@ResponseBody
	public TaotaoResult logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		
		try {
			
			logoutService.logout(request, response, session);
			response.sendRedirect(HOME_PAGE_URL);
			
			return TaotaoResult.ok();
		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
	
}
