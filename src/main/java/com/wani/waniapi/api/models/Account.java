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

@Document(collection = "accounts")
public class Account {
	
	
		@Autowired
	    UserRepository userRepository;
	    
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
		
		public AccountResponse getRequestResponse() {
			AccountResponse accountResponse = new AccountResponse();
			accountResponse.setId(this.id);
			accountResponse.setAmount(this.amount);
			accountResponse.setCreatedAt(this.createdAt);
//			Optional<User> user =  userRepository.findById(this.userId);
//	        if(user.isPresent()){
////	        	accountResponse.setUserResponse(user.get().getRequestResponse());
//	        }
	        return accountResponse;
		}

}
