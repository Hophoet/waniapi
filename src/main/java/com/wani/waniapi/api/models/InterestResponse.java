package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;

public class InterestResponse {
	@Id
    private String id;
    private String subscriptionId;
	private int amount;
	private int totalAmount;
    private long time;
    private long lastPaymentAt;
    private long remainingDays;
	private boolean paid;

    
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	public long getLastPaymentAt() {
		return lastPaymentAt;
	}
	public void setLastPaymentAt(long lastPaymentAt) {
		this.lastPaymentAt = lastPaymentAt;
	}
	public long getRemainingDays() {
		return remainingDays;
	}
	public void setRemainingDays(long remainingDays) {
		this.remainingDays = remainingDays;
	}

}
