package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public class WithdrawalResponse {
	@Id
	private String id;
	private AccountResponse account;
	private PaymentMethodResponse paymentMethod;
	private int amount;
	private LocalDateTime createdAt;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AccountResponse getAccount() {
		return account;
	}
	public void setAccount(AccountResponse account) {
		this.account = account;
	}
	public PaymentMethodResponse getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethodResponse paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	

}
