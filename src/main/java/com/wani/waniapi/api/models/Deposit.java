package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deposits")
public class Deposit {
	 @Id
	 private String id;
	 private String accountId;
	 private int amount;
	 private String paymentMethodId;
	 private LocalDateTime createdAt;
	 
	 public Deposit(int amount, String accountId, String paymentMethodId) {
		 this.amount = amount;
		 this.accountId = accountId;
		 this.paymentMethodId = paymentMethodId;
		 this.createdAt = LocalDateTime.now();
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

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


}
