package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

	TbItem getItemById(Long itemId);

	EasyUIDataGridResult getItemList(int page, int rows);

	TaotaoResult createItem(TbItem item, String desc, String itemParam);

	String getItemParamHtml(Long itemId);

	TaotaoResult deleteItemById(Long itemId);

	TaotaoResult delete(String[] ids);

	TaotaoResult update(TbItem po, String des, String itemparam);

	TaotaoResult instock(List<Long> ids);

	TaotaoResult reshelf(List<Long> ids);
}
