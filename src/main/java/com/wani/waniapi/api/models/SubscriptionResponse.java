package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;


public class SubscriptionResponse {
	@Id
	private String id;
	private AccountResponse account;
	private SubscriptionPlanResponse subscriptionPlan;
	private PaymentResponse payment;
	private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private LocalDateTime lastInterestPaymentAt;
    private LocalDateTime nextInterestPaymentAt;
    private boolean paid = false;
    private int timeRemaining ;
	 
	public String getId() {
		return id;
	}


	public boolean isPaid() {
		return paid;
	}
	public void setId(String id) {
		this.id = id;
	}



	
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	public PaymentResponse getPayment() {
		return payment;
	}
	public void setPayment(PaymentResponse payment) {
		this.payment = payment;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getTimeRemaining() {
		return timeRemaining;
	}
	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getEndedAt() {
		return endedAt;
	}
	public void setEndedAt(LocalDateTime endedAt) {
		this.endedAt = endedAt;
	}

	public AccountResponse getAccount() {
		return account;
	}

	public void setAccount(AccountResponse account) {
		this.account = account;
	}


	public SubscriptionPlanResponse getSubscriptionPlan() {
		return subscriptionPlan;
	}


	public void setSubscriptionPlan(SubscriptionPlanResponse subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
	}


	public LocalDateTime getLastInterestPaymentAt() {
		return lastInterestPaymentAt;
	}


	public void setLastInterestPaymentAt(LocalDateTime lastInterestPaymentAt) {
		this.lastInterestPaymentAt = lastInterestPaymentAt;
	}


	public LocalDateTime getNextInterestPaymentAt() {
		return nextInterestPaymentAt;
	}


	public void setNextInterestPaymentAt(LocalDateTime nextInterestPaymentAt) {
		this.nextInterestPaymentAt = nextInterestPaymentAt;
	}




}
