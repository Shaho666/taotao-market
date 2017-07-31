package com.taotao.rest.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 商品分类节点
 */
public class ItemCatNode {
	
	@JsonProperty("u")
	private String url;
	@JsonProperty("n")
	private String name;
	@JsonProperty("i")
	private List<?> items;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<?> getItems() {
		return items;
	}
	public void setItems(List<?> items) {
		this.items = items;
	}
	
	

}
