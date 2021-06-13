package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;

import com.wani.waniapi.auth.models.User;

public class PaymentResponse {
	@Id
	private String id;
	private User user;
	private PaymentMethod paymentMethod;
	private Integer createdAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public Integer getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Integer createdAt) {
		this.createdAt = createdAt;
	}
	

}
