package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;

public class Account {
	   @Id
	    private String id;
	    private String userId;
	    private int amount;
	    
	    public Account(String userId) {
	    	this.userId = userId;
	    	this.amount = 0;
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

}
