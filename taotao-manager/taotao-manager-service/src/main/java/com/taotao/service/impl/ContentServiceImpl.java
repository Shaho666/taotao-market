package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Override
	public TaotaoResult insertContent(TbContent content) {
		
		Date date = new Date();
		
		content.setCreated(date);
		content.setUpdated(date);
		
		contentMapper.insert(content);
		
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(int page, int rows) {
		
		PageHelper.startPage(page, rows);
		
		TbContentExample example = new TbContentExample();
		List<TbContent> list = contentMapper.selectByExample(example);
		
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		
		return result;
	}
	public TaotaoResult deleteContent(String[] ids) {
		TaotaoResult op = new TaotaoResult();
		TbContentExample ex = new TbContentExample();
		List<Long> list = new ArrayList<Long>();
		com.taotao.pojo.TbContentExample.Criteria c=ex.createCriteria();
		for (String string : ids) {
			Long id = Long.valueOf(string);
			list.add(id);
		}
		c.andIdIn(list);
		int x = contentMapper.deleteByExample(ex);
		if(x>0) {
			op.setStatus(200);
		}else {
			op.setStatus(500);
		}
		return op;
	}
	/**
	 * 内容管理的编辑功能
	 */
	public TaotaoResult contentEdit(TbContent content) {
		TaotaoResult result=new TaotaoResult();
		TbContentExample ex=new TbContentExample();
		content.setUpdated(new Date());
		com.taotao.pojo.TbContentExample.Criteria c=ex.createCriteria();
		int x=contentMapper.updateByPrimaryKeySelective(content);
		if(x>0) {
			result.setStatus(200);
		}else {
			result.setStatus(500);
		}
		return result;
	}
}
