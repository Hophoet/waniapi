package com.wani.waniapi.api.models;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.models.UserResponse;
import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.services.UserService;

@Document(collection = "perfectMoneyAccounts")
public class PerfectMoneyAccount {
	
	   @Id
	    private String id;
	    private String userId;
	    private String widthdrawalAccount;
	    private LocalDateTime createdAt;
	    
	    public PerfectMoneyAccount(String userId, String widthdrawalAccount) {
	    	this.createdAt = LocalDateTime.now();
	    	this.userId = userId;
	    	this.widthdrawalAccount = widthdrawalAccount;
	    }

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

		public String getWidthdrawalAccount() {
			return widthdrawalAccount;
		}

		public void setWidthdrawalAccount(String widthdrawalAccount) {
			this.widthdrawalAccount = widthdrawalAccount;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

}
