package com.wani.waniapi.api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wani.waniapi.api.repositories.SubscriptionRepository;

@Document(collection = "subscriptionPlans")
public class SubscriptionPlan {
    @Autowired
    SubscriptionRepository subscriptionRepository;

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

    public SubscriptionPlan(){

    }

    public SubscriptionPlan(
        String name, 
        String description, 
        Integer minAmount,
        Integer maxAmount,
        Integer frequency,
        Integer roip,
        Integer duration
    ) {
        this.name = name;
        this.description = description;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.roip = roip;
        this.duration = duration;
        this.frequency = frequency;

    	this.createdAt = LocalDateTime.now();
        this.available = true;
    }
    public SubscriptionPlan(
        String name, 
        String description, 
        Integer minAmount,
        Integer maxAmount,     
        Integer frequency,
        Integer roip,
        Integer duration,
        Boolean available
    ) {
        this.name = name;
        this.description = description;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.roip = roip;
        this.duration = duration;
        this.frequency = frequency;
    	this.createdAt = LocalDateTime.now();
        this.available = available;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	

	public Integer getRoip() {
		return roip;
	}

	public void setRoip(Integer roip) {
		this.roip = roip;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Integer getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}

	public Integer getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Integer maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	public SubscriptionPlanResponse getRequestResponse() {
		SubscriptionPlanResponse subscriptionPlanResponse = new SubscriptionPlanResponse();
		subscriptionPlanResponse.setId(this.id);
		subscriptionPlanResponse.setAvailable(this.available);
		subscriptionPlanResponse.setCreatedAt(this.createdAt);
		subscriptionPlanResponse.setDescription(this.description);
		subscriptionPlanResponse.setDuration(this.duration);
		subscriptionPlanResponse.setFrequency(this.frequency);
		subscriptionPlanResponse.setMaxAmount(this.maxAmount);
		subscriptionPlanResponse.setMinAmount(this.minAmount);
		subscriptionPlanResponse.setRoip(this.roip);
		return subscriptionPlanResponse;
	}
    
  
   
}
