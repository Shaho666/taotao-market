package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentCatgoryService;
import com.taotao.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentCatgoryService contentcatgoryser;
	
	/*http://localhost:8081/rest*/
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	
	/*/sync/content/*/
	@Value("${REST_COTENT_SYNC_URL}")
	private String REST_COTENT_SYNC_URL;
	
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult insertContent(TbContent content) {
		TaotaoResult result = contentService.insertContent(content);
		HttpClientUtil.doGet(REST_BASE_URL + REST_COTENT_SYNC_URL + content.getCategoryId());
		return result;
	}
	
	@RequestMapping("/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(@RequestParam(value="categoryId") long id,Integer page,Integer rows) {
		EasyUIDataGridResult result = contentcatgoryser.querylist(id, page, rows);
		return result;
	}
	@RequestMapping("delete")
	@ResponseBody
	public TaotaoResult deleteContent(String ids){
		String[] array=ids.split(",");
		return contentService.deleteContent(array);
	}

}
