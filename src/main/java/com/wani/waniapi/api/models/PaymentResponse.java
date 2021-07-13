package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.models.UserResponse;

public class PaymentResponse {
	@Id
	private String id;
	private AccountResponse account;
	private PaymentMethodResponse paymentMethod;
    private LocalDateTime createdAt;
    private Integer amount;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
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

}
