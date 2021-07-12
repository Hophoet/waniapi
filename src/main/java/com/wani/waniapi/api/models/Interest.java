package com.wani.waniapi.api.models;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "interests")
public class Interest {
    @Id
    private String id;
    private String subscriptionId;
    private String accountId;
	private int amount;
    private LocalDateTime createdAt;
    private boolean paid;
    
    public Interest(
    		String subscriptionId, 
    		String accountId, 
    		int amount
    		) {
    	this.subscriptionId = subscriptionId;
    	this.accountId = accountId;
    	this.amount = amount;
    	this.setCreatedAt(LocalDateTime.now());
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}


	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


}
