package com.wani.waniapi.auth.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "passwordResets")
public class PasswordReset {
	 @Id
	 private String id;
	 private UUID token;
	 private LocalDateTime createdAt;
	 
	 public PasswordReset() {
		 this.token = UUID.randomUUID();
		 this.createdAt = LocalDateTime.now();
	 }
	 
	 public void reset() {
		 this.token = UUID.randomUUID();
		 this.createdAt = LocalDateTime.now();
	 }
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UUID getToken() {
		return token;
	}
	public void setToken(UUID token) {
		this.token = token;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	

}
