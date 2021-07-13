package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "subscriptions")
public class Subscription {
    @Id
    private String id;
    private String accountId;
    private String subscriptionPlanId;
    private String paymentId;
	private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;

    private LocalDateTime lastInterestPaymentAt;
    private LocalDateTime nextInterestPaymentAt;
    private boolean paid = false;
    private int timeRemaining ;
    
    public Subscription(
        String accountId, 
        int amount,
        String subscriptionPlanId, 
        String paymentId
    ) {
        this.setAccountId(accountId);
        this.subscriptionPlanId = subscriptionPlanId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

 

    public String getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(String subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    
  
    
    public void setPaid(boolean paid) {
		this.paid = paid;
	}
    
    public boolean getPaid() {
    	return paid;
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



	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	public LocalDateTime getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(LocalDateTime endedAt) {
		this.endedAt = endedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
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
