package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "withdrawals")
public class Withdrawal {
	@Id
    private String id;
	private String userId;
    private String paymentMethodId;
    private String phoneNumber;
	private int amount;
    private long createdAt;
    
    public Withdrawal(
    		String userId,
    		String phoneNumber,
    		String paymentMethodId,
    		int amount
    		) {
    	this.userId = userId;
    	this.phoneNumber = phoneNumber;
    	this.setPaymentMethodId(paymentMethodId);
    	this.amount = amount;
    	this.createdAt = System.currentTimeMillis();
    }
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

}
