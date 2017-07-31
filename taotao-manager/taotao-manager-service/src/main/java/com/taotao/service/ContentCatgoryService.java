package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

public interface ContentCatgoryService {

	List<EasyUITreeNode> getContentCatList(Long parentId);
	
	TaotaoResult insertCatgory(Long parentId, String name);
	
	TaotaoResult updateCatgory(Long id, String name);
	
	/**
	 * liubaichuan添加， 2017-07-29
	 * @param parentId 删除类目的父节点id
	 * @param id 删除类目的id
	 * @return
	 */
	TaotaoResult deleteCatgory(Long parentId, Long id);
	/**
	 * xzd
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult querylist(long id, int page, int rows);
}
