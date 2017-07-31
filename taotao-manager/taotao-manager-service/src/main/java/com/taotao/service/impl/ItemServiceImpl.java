package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;

	@Override
	public TbItem getItemById(Long itemId) {

		TbItemExample example = new TbItemExample();

		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);

		List<TbItem> list = itemMapper.selectByExample(example);

		TbItem tbItem = null;
		if (list != null && list.size() > 0) {
			tbItem = list.get(0);
		}

		return tbItem;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {

		PageHelper.startPage(page, rows);

		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);

		PageInfo<TbItem> pageInfo = new PageInfo<>(list);

		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());

		return result;
	}

	@Override
	public TaotaoResult createItem(TbItem item, String desc, String itemParam) {

		long itemId = IDUtils.genItemId();

		item.setId(itemId);
		item.setStatus((byte) 1);

		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		itemMapper.insert(item);

		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		itemDesc.setItemDesc(desc);
		itemDescMapper.insert(itemDesc);

		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParam);
		itemParamItem.setCreated(date);
		itemParamItem.setUpdated(date);
		itemParamItemMapper.insert(itemParamItem);

		return TaotaoResult.ok();
	}

	@Override
	public String getItemParamHtml(Long itemId) {

		TbItemParamItemExample example = new TbItemParamItemExample();
		com.taotao.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);

		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);

		if (list == null || list.isEmpty()) {
			return "";
		}

		TbItemParamItem itemParamItem = list.get(0);
		String paramData = itemParamItem.getParamData();

		@SuppressWarnings("rawtypes")
		List<Map> mapList = JsonUtils.jsonToList(paramData, Map.class);

		StringBuffer strbuf = new StringBuffer();

		strbuf.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
		strbuf.append("    <tbody>\n");

		for (@SuppressWarnings("rawtypes")
		Map map : mapList) {

			strbuf.append("	    <tr>\n");
			strbuf.append("		    <th class=\"tdTitle\" colspan=\"2\">" + map.get("group") + "</th>\n");
			strbuf.append("		</tr>\n");

			@SuppressWarnings({ "rawtypes", "unchecked" })
			List<Map> mapList2 = (List<Map>) map.get("params");
			for (@SuppressWarnings("rawtypes")
			Map map2 : mapList2) {
				strbuf.append("		<tr>\n");
				strbuf.append("		    <td class=\"tdTitle\">" + map2.get("k") + "</td>\n");
				strbuf.append("			<td>" + map2.get("v") + "</td>\n");
				strbuf.append("		</tr>\n");
			}

		}

		strbuf.append("	</tbody>\n");
		strbuf.append("</table>");

		return strbuf.toString();
	}

	@Override
	public TaotaoResult deleteItemById(Long itemId) {

		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);

		itemMapper.deleteByExample(example);

		return TaotaoResult.ok();
	}

	/**
	 * 更新商品信息、商品描述、商品规格 （商品编辑）
	 */
	public TaotaoResult update(TbItem po, String des, String itemparam) {
		po.setUpdated(new Date());
		int x = itemMapper.updateByPrimaryKeySelective(po);
		TbItemDesc desc = new TbItemDesc();
		desc.setItemId(po.getId());
		desc.setUpdated(new Date());
		desc.setItemDesc(des);
		itemDescMapper.updateByPrimaryKeySelective(desc);
		TbItemParamItem tParamItem = new TbItemParamItem();
		TbItemParamItem item = findbyitemid(po.getId());
		tParamItem.setId(item.getId());
		tParamItem.setUpdated(new Date());
		tParamItem.setParamData(itemparam);
		itemParamItemMapper.updateByPrimaryKeySelective(tParamItem);

		TaotaoResult op = new TaotaoResult();
		if (x > 0) {
			op.setStatus(200);

		} else {
			op.setStatus(500);

		}
		return op;
	}

	/**
	 * 根据商品id查询出规格对象
	 */
	public TbItemParamItem findbyitemid(Long id) {
		TbItemParamItemExample ex = new TbItemParamItemExample();
		com.taotao.pojo.TbItemParamItemExample.Criteria c = ex.createCriteria();
		c.andItemIdEqualTo(id);
		List<TbItemParamItem> item = itemParamItemMapper.selectByExampleWithBLOBs(ex);
		return item.get(0);
	}

	/**
	 * 根据商品id删除商品
	 */
	public TaotaoResult delete(String[] ids) {

		TaotaoResult op = new TaotaoResult();
		TbItemExample ex = new TbItemExample();
		List<Long> list = new ArrayList<Long>();
		Criteria c = ex.createCriteria();
		for (String string : ids) {
			Long id = Long.valueOf(string);
			list.add(id);
		}
		c.andIdIn(list);
		int x = itemMapper.deleteByExample(ex);

		// 删除商品描述
		TbItemDescExample descex = new TbItemDescExample();
		com.taotao.pojo.TbItemDescExample.Criteria desc = descex.createCriteria();
		desc.andItemIdIn(list);
		int x1 = itemDescMapper.deleteByExample(descex);
		// 删除商品规格信息
		TbItemParamItemExample paramItemex = new TbItemParamItemExample();
		com.taotao.pojo.TbItemParamItemExample.Criteria c2 = paramItemex.createCriteria();
		c2.andItemIdIn(list);
		int x2 = itemParamItemMapper.deleteByExample(paramItemex);
		if (x > 0 && x1 > 0 && x2 > 0) {
			op.setStatus(200);
		} else {
			op.setStatus(500);
		}
		return op;
	}

	// 下架商品
	public TaotaoResult instock(Long ids) {
		TaotaoResult result = new TaotaoResult();
		TbItem item = new TbItem();
		item.setStatus(new Byte("2"));
		item.setId(ids);
		int x = itemMapper.updateByPrimaryKeySelective(item);
		if (x > 0) {
			result.setStatus(200);
		} else {
			result.setStatus(500);
		}
		return result;
	}

	public TaotaoResult reshelf(Long ids) {
		TaotaoResult result = new TaotaoResult();
		TbItem item = new TbItem();
		item.setStatus(new Byte("1"));
		item.setId(ids);
		int x = itemMapper.updateByPrimaryKeySelective(item);
		if (x > 0) {
			result.setStatus(200);
		} else {
			result.setStatus(500);
		}
		return result;
	}
}
