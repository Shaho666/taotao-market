package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private JedisClient jedisClient;

	/*REDIS_CONTENT*/
	@Value("${REDIS_CONTENT_KEY}")
	private String REDIS_CONTENT_KEY;

	/*86400*/
	@Value("${REDIS_TAOTAO_ITME_EXPIRE}")
	private Integer REDIS_TAOTAO_ITME_EXPIRE;

	@Override
	public TaotaoResult getContentList(long categoryId) {
		// 添加取缓存的逻辑
		// 缓存不能影响正常逻辑
		try {
			String result = jedisClient.hget(REDIS_CONTENT_KEY, categoryId + "");
			
			// 判断结果是否为空
			if (!StringUtils.isBlank(result)) {
				List<TbContent> list = JsonUtils.jsonToList(result, TbContent.class);
				return TaotaoResult.ok(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存逻辑结束
		List<TbContent> list = null;

		try {
			TbContentExample example = new TbContentExample();
			Criteria criteria = example.createCriteria();
			// 设置查询条件
			criteria.andCategoryIdEqualTo(categoryId);

			// 执行查询
			list = contentMapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		// 把内容添加到缓存中
		try {
			jedisClient.hset(REDIS_CONTENT_KEY, categoryId + "", JsonUtils.objectToJson(list));
			jedisClient.expire(REDIS_CONTENT_KEY, REDIS_TAOTAO_ITME_EXPIRE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// end
		
		return TaotaoResult.ok(list);
	}

}
