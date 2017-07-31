package com.taotao.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ContentService;
import com.taotao.service.ItemDescService;
import com.taotao.service.ItemParamItemService;
import com.taotao.service.ItemService;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
/**
 * 编辑商品
 * @author xuezhendong
 *2017年7月23日
 */
@Controller
@RequestMapping("rest")
public class RestItemController {
	@Autowired
	ItemDescService descser;
	@Autowired
	ItemParamItemService itemparamser;
	@Autowired
	ItemService tbitemser;
	@Autowired
	ContentService contentser;
	
	/**
	 * 进入编辑页面
	 * autor:xuezhendong
	 * 2017年7月28日
	 */
	@RequestMapping("page/{pager}")
	public String pagerUtil(@PathVariable String pager){
		return pager;
	}
	@RequestMapping("item/query/item/desc/{id}")
	@ResponseBody
	public TaotaoResult queryitemdesc(@PathVariable(value="id") Long id){
		TbItemDesc m=descser.getItemDescById(id);
		TaotaoResult result=new TaotaoResult();
		if(m!=null) {
			result.setStatus(200);
			result.setData(m);
		}else {
			result.setStatus(500);
		}
		return result;
	}
	
	@RequestMapping("item/param/item/query/{id}")
	@ResponseBody
	public TaotaoResult queryitemparam(@PathVariable(value="id") Long id){
		return itemparamser.findbyid(id);
	}
	
	@RequestMapping("item/update")
	@ResponseBody
	public TaotaoResult update(TbItem po,String desc,String itemParams){
		System.out.println(itemParams);
		return tbitemser.update(po,desc,itemParams);
	}
	
	@RequestMapping("item/delete")
	@ResponseBody
	public TaotaoResult delete(String ids){
		String[] array=ids.split(",");
		return tbitemser.delete(array);
	}
	//下架商品
	@RequestMapping("item/instock")
	@ResponseBody
	public TaotaoResult instock(Long ids){
		return tbitemser.instock(ids);
	}
	//上架商品
	@RequestMapping("item/reshelf")
	@ResponseBody
	public TaotaoResult reshelf(Long ids){
		return tbitemser.reshelf(ids);
	}
	//内容管理的编辑功能
	@RequestMapping("content/edit")
	@ResponseBody
	public TaotaoResult contentEdit(TbContent content) {
		return contentser.contentEdit(content);
	}
}
