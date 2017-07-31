package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentCatgoryService;

@Service
public class ContentCatgoryServiceImpl implements ContentCatgoryService {

	@Autowired
	private TbContentCategoryMapper contentCatgoryMapper;
	@Autowired
	private TbContentMapper contentMapper;

	@Override
	public List<EasyUITreeNode> getContentCatList(Long parentId) {

		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);

		List<TbContentCategory> list = contentCatgoryMapper.selectByExample(example);

		List<EasyUITreeNode> resultList = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			resultList.add(node);
		}

		return resultList;
	}

	@Override
	public TaotaoResult insertCatgory(Long parentId, String name) {

		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setStatus(1);
		contentCategory.setIsParent(false);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());

		contentCatgoryMapper.insert(contentCategory);
		Long id = contentCategory.getId();

		TbContentCategory parentNode = contentCatgoryMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			contentCatgoryMapper.updateByPrimaryKey(parentNode);
		}

		return TaotaoResult.ok(id);
	}

	@Override
	public TaotaoResult updateCatgory(Long id, String name) {
		// 修改后的内容不能为空
		if ("".equals(name.trim())) {
			TaotaoResult reuslt = new TaotaoResult();
			reuslt.setStatus(500);
			reuslt.setMsg("内容不能为空！");
			return reuslt;
		}
		TbContentCategory contentCategory = contentCatgoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		contentCategory.setUpdated(new Date());
		contentCatgoryMapper.updateByPrimaryKey(contentCategory);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteCatgory(Long parentId, Long id) {
		// 递归删除类目
		recursiveDelete(id);
		// 检查父节点下是否还有类目，如果没有将其设为非父节点
		TbContentCategory asParentExample = new TbContentCategory();
		asParentExample.setParentId(parentId);
		List<TbContentCategory> list = contentCatgoryMapper.list(asParentExample);
		if (list == null || list.size() < 1) {
			TbContentCategory parent = contentCatgoryMapper.selectByPrimaryKey(parentId);
			parent.setIsParent(false);
			parent.setUpdated(new Date());
			contentCatgoryMapper.updateByPrimaryKeySelective(parent);
		}

		return null;
	}

	/**
	 * liubaichuan添加， 2017-07-29 递归删除节点
	 * 
	 * @param id
	 */
	private void recursiveDelete(Long id) {
		TbContentCategory example = contentCatgoryMapper.selectByPrimaryKey(id);
		TbContentCategory asParentExample = new TbContentCategory();
		asParentExample.setParentId(id);
		if (example != null) {
			List<TbContentCategory> list = contentCatgoryMapper.list(asParentExample);
			if (list != null && list.size() > 0)
				for (TbContentCategory tbContentCategory : list) {
					recursiveDelete(tbContentCategory.getId());
				}

		}
		contentCatgoryMapper.deleteByPrimaryKey(id);
		// 删除广告内容
		contentMapper.deleteByCategoryId(id);
	}

	/**
	 * 根据内容id，分页查询
	 */
	public EasyUIDataGridResult querylist(long id, int page, int rows) {
		TbContentExample ex = new TbContentExample();
		com.taotao.pojo.TbContentExample.Criteria c = ex.createCriteria();
		c.andCategoryIdEqualTo(id);
		PageHelper.startPage(page, rows);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(ex);
		PageInfo<TbContent> info = new PageInfo<TbContent>(list);
		EasyUIDataGridResult m = new EasyUIDataGridResult();
		m.setRows(list);
		m.setTotal(info.getTotal());
		return m;
	}
}
