package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public class SubscriptionPlanResponse {
	@Id
    private String id;
    private String name;
    private String description;
    private Integer min_amount;
    private Integer max_amount;
    private Integer frequency;
    private Integer roip;
    private Boolean available;
    private Integer duration;
    private LocalDateTime createdAt;
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
	public Integer getMin_amount() {
		return min_amount;
	}
	public void setMin_amount(Integer min_amount) {
		this.min_amount = min_amount;
	}
	public Integer getMax_amount() {
		return max_amount;
	}
	public void setMax_amount(Integer max_amount) {
		this.max_amount = max_amount;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	public Integer getRoip() {
		return roip;
	}
	public void setRoip(Integer roip) {
		this.roip = roip;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getSubscriptionsCount() {
		return subscriptionsCount;
	}
	public void setSubscriptionsCount(Integer subscriptionsCount) {
		this.subscriptionsCount = subscriptionsCount;
	}

}
