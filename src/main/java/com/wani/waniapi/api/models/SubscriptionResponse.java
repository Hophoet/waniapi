package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;

import com.wani.waniapi.auth.models.User;

public class SubscriptionResponse {
	@Id
	private String id;
	private User user;
	private SubscriptionPlan subscriptionPlan;
	private Payment payment;
	private String phoneNumber;
	private long createdAt;
	private long endedAt;
	private boolean paid = false;
	public String getId() {
		return id;
	}
	public User getUser() {
		return user;
	}
	public SubscriptionPlan getSubscriptionPlan() {
		return subscriptionPlan;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public long getCreatedAt() {
		return createdAt;
	}
	public long getEndedAt() {
		return endedAt;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	public void setEndedAt(long endedAt) {
		this.endedAt = endedAt;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}


}
