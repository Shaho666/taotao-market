package com.taotao.rest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;
import com.taotao.pojo.TbOrderExample.Criteria;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderItemExample;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.pojo.TbOrderShippingExample;
import com.taotao.rest.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Override
	public TaotaoResult getOrderByUserId(Long userId) {
		
		TbOrderExample example = new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		criteria.andUserIdEqualTo(userId);
		
		List<TbOrder> list = orderMapper.selectByExample(example);
		
		return TaotaoResult.ok(list);
	}

	@Override
	public TaotaoResult getOrderItemListByOrderId(String orderId) {
		
		TbOrderItemExample example = new TbOrderItemExample();
		com.taotao.pojo.TbOrderItemExample.Criteria criteria = example.createCriteria();
		
		criteria.andOrderIdEqualTo(orderId);
		
		List<TbOrderItem> list = orderItemMapper.selectByExample(example);
		
		return TaotaoResult.ok(list);
	}

	@Override
	public TaotaoResult getOrderShippingListByOrderId(String orderId) {
		
		TbOrderShippingExample example = new TbOrderShippingExample();
		com.taotao.pojo.TbOrderShippingExample.Criteria criteria = example.createCriteria();
		
		criteria.andOrderIdEqualTo(orderId);
		
		List<TbOrderShipping> list = orderShippingMapper.selectByExample(example);
		
		return TaotaoResult.ok(list);
	}

}
