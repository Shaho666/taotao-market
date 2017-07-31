package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;

public interface ItemParamService {

	EasyUIDataGridResult getItemParamList(int page, int rows);
	
	TaotaoResult getItemParamByCid(Long cid);
	
	TaotaoResult insertItemParam(Long cid, String paramData);
	/**
	 * xuezhendong
	 * @param ids
	 * @return
	 */
	TaotaoResult Deleteitemparam(String[] ids);
	/**
	 * 批量获取包含类目信息的ItemParam
	 * liubaichuan添加，2017-07-30
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult getItemParamVOList(int page, int rows);
	
	/**
	 * 更新ItemParam
	 * liubaichuan添加，2017-07-31
	 * @param eid
	 * @param paramData
	 * @return
	 */
	TaotaoResult updateItemParam(Long eid, String paramData);
	
}
