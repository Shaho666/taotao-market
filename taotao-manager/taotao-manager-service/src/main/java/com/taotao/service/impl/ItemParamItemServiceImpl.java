package com.taotao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.service.ItemParamItemService;
@Service
public class ItemParamItemServiceImpl implements ItemParamItemService{
	@Autowired
	private TbItemParamItemMapper mapper;

	/**
	 * 根据商品id查询规格item
	 */
	public TaotaoResult findbyid(Long id) {
		TaotaoResult m = new TaotaoResult();
		TbItemParamItemExample ex = new TbItemParamItemExample();
		Criteria c = ex.createCriteria();
		c.andItemIdEqualTo(id);
		List<TbItemParamItem> list = mapper.selectByExampleWithBLOBs(ex);
		if (list != null && list.size() > 0) {
			m.setData(list.get(0));
			m.setStatus(200);
		} else {
			m.setStatus(500);
		}
		return m;
	}
}
