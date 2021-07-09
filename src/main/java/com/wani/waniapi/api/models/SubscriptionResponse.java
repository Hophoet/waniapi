package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;


public class SubscriptionResponse {
	@Id
	private String id;
	private Account account;
	private SubscriptionPlan subscriptionPlan;
	private PaymentResponse payment;
	private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private long lastInterestPaymentAt;
    private long nextInterestPaymentAt;
    private boolean paid = false;
    private int timeRemaining ;
	 
	public String getId() {
		return id;
	}

	public SubscriptionPlan getSubscriptionPlan() {
		return subscriptionPlan;
	}

	public boolean isPaid() {
		return paid;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
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
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
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
	public long getLastInterestPaymentAt() {
		return lastInterestPaymentAt;
	}
	public void setLastInterestPaymentAt(long lastInterestPaymentAt) {
		this.lastInterestPaymentAt = lastInterestPaymentAt;
	}
	public long getNextInterestPaymentAt() {
		return nextInterestPaymentAt;
	}
	public void setNextInterestPaymentAt(long nextInterestPaymentAt) {
		this.nextInterestPaymentAt = nextInterestPaymentAt;
	}




}
