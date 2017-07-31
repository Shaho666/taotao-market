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
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamCategoryVO;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.service.ItemParamService;

@Service
public class ItemParamServiceImpl implements ItemParamService {

	@Autowired
	private TbItemParamMapper itemParamMapper;

	@Override
	public EasyUIDataGridResult getItemParamList(int page, int rows) {

		PageHelper.startPage(page, rows);

		TbItemParamExample example = new TbItemParamExample();
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);

		PageInfo<TbItemParam> pageInfo = new PageInfo<TbItemParam>(list);

		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());

		return result;

	}

	@Override
	public TaotaoResult getItemParamByCid(Long cid) {

		TbItemParamExample example = new TbItemParamExample();

		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);

		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);

		if (list != null && list.size() > 0) {
			TbItemParam itemParam = list.get(0);
			return TaotaoResult.ok(itemParam);
		}

		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult insertItemParam(Long cid, String paramData) {

		TbItemParam itemParam = new TbItemParam();
		itemParam.setItemCatId(cid);
		itemParam.setParamData(paramData);
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());

		itemParamMapper.insert(itemParam);

		return TaotaoResult.ok();
	}

	public TaotaoResult Deleteitemparam(String[] ids) {
		TaotaoResult op = new TaotaoResult();
		TbItemParamExample ex = new TbItemParamExample();
		List<Long> list = new ArrayList<Long>();
		Criteria c = ex.createCriteria();
		for (String string : ids) {
			Long id = Long.valueOf(string);
			list.add(id);
		}
		c.andIdIn(list);
		int x = itemParamMapper.deleteByExample(ex);
		if (x > 0) {
			op.setStatus(200);
		} else {
			op.setStatus(500);
		}
		return op;
	}
	@Override
	public EasyUIDataGridResult getItemParamVOList(int page, int rows) {
		PageHelper.startPage(page, rows);

		TbItemParamExample example = new TbItemParamExample();
		List<TbItemParamCategoryVO> list = itemParamMapper.list();

		PageInfo<TbItemParamCategoryVO> pageInfo = new PageInfo<TbItemParamCategoryVO>(list);

		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());

		return result;
	}

	@Override
	public TaotaoResult updateItemParam(Long eid, String paramData) {

		TbItemParam itemParam = new TbItemParam();
		itemParam.setId(eid);
		itemParam.setParamData(paramData);
		itemParam.setUpdated(new Date());
		itemParamMapper.updateByPrimaryKeySelective(itemParam);
		return TaotaoResult.ok();
	}
}
