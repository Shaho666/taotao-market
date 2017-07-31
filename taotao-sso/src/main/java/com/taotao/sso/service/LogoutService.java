package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.taotao.common.pojo.TaotaoResult;

public interface LogoutService {

	TaotaoResult logout(HttpServletRequest request, HttpServletResponse response, HttpSession session);
	
}
