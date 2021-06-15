package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "interests")
public class Interest {
    @Id
    private String id;
    private String subscriptionId;
	private int amount;
    private long createdAt;
    
    public Interest(String subscriptionId, int amount) {
    	this.subscriptionId = subscriptionId;
    	this.setAmount(amount);
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

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
}
