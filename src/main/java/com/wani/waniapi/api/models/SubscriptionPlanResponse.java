package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;

public class SubscriptionPlanResponse {
	@Id
    private String id;
    private String name;
    private String description;
    private Integer amount;
    private Integer interest;
    private Boolean available;
    private Integer duration;
    private Integer createdAt;
    private Integer subscriptionsCount;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getInterest() {
		return interest;
	}
	public void setInterest(Integer interest) {
		this.interest = interest;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Integer createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getSubscriptionsCount() {
		return subscriptionsCount;
	}
	public void setSubscriptionsCount(Integer subscriptionsCount) {
		this.subscriptionsCount = subscriptionsCount;
	}
}
