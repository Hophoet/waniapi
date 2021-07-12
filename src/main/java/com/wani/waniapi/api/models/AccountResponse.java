package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wani.waniapi.auth.models.UserResponse;

@Document(collection = "accounts")
public class AccountResponse {
	   @Id
	    private String id;
	    private UserResponse user;
	    private LocalDateTime createdAt;
	    private int amount;
	    
	    public AccountResponse() {
	    	this.amount = 0;
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
		
		public void addAmount(int amount) {
			this.amount += amount;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public UserResponse getUser() {
			return user;
		}

		public void setUser(UserResponse user) {
			this.user = user;
		}

}
