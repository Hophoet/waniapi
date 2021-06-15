package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "subscriptions")
public class Subscription {
    @Id
    private String id;
    private String userId;
    private String subscriptionPlanId;
    private String paymentId;
    private String phoneNumber;
	private int amount;
    private long createdAt;
    private long endedAt;
    private boolean paid = false;
    private int durationRemaining ;
    
    public Subscription(
        String userId, 
        String subscriptionPlanId, 
        String paymentId,
        String phoneNumber
    ) {
        this.userId = userId;
        this.subscriptionPlanId = subscriptionPlanId;
        this.paymentId = paymentId;
        Date currentDate = new Date();
        this.createdAt = currentDate.getTime();
        this.phoneNumber = phoneNumber;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }
    
    public String getPhoneNumber() {
		return phoneNumber;
	}
    
    public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public int getDurationRemaining() {
		return durationRemaining;
	}

	public void setDurationRemaining(int durationRemaining) {
		this.durationRemaining = durationRemaining;
	}


    
   

}
