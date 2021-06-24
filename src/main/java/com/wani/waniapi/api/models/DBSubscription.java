package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;

public class DBSubscription {
	@Id
    private String id;
	
	public DBSubscription(String id) {
		this.id = id;
	}
	public DBSubscription() {
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
