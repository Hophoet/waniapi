package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public class Account {
	   @Id
	    private String id;
	    private String userId;
	    private LocalDateTime createdAt;
	    private int amount;
	    
	    public Account(String userId) {
	    	this.userId = userId;
	    	this.amount = 0;
	    	this.createdAt = LocalDateTime.now();
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
		
		public void addAmount(int amount) {
			this.amount += amount;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

}
