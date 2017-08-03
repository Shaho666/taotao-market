package com.taotao.portal.pojo;

import java.util.Date;

public class OrderItem {

	private String orderId;
	private String receiverName;
	private Long tottalPrice;
	private Date Created;
	private Integer status;
	private String itemId;
	private String image;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public Long getTottalPrice() {
		return tottalPrice;
	}
	public void setTottalPrice(Long tottalPrice) {
		this.tottalPrice = tottalPrice;
	}
	public Date getCreated() {
		return Created;
	}
	public void setCreated(Date created) {
		Created = created;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
