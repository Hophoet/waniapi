package com.wani.waniapi.api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public class SubscriptionPlanResponse {
	@Id
    private String id;
    private String name;
    private String description;
    private Integer minAmount;
    private Integer maxAmount;
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
	public Integer getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Integer maxAmount) {
		this.maxAmount = maxAmount;
	}
	public Integer getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}

}
