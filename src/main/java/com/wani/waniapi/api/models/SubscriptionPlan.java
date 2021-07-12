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
    private Integer min_amount;
    private Integer max_amount;
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
        Integer min_amount,
        Integer max_amount,
        Integer frequency,
        Integer roip,
        Integer duration
    ) {
        this.name = name;
        this.description = description;
        this.max_amount = max_amount;
        this.min_amount = min_amount;
        this.roip = roip;
        this.duration = duration;
        this.frequency = frequency;

    	this.createdAt = LocalDateTime.now();
        this.available = true;
    }
    public SubscriptionPlan(
        String name, 
        String description, 
        Integer min_amount,
        Integer max_amount,     
        Integer frequency,
        Integer roip,
        Integer duration,
        Boolean available
    ) {
        this.name = name;
        this.description = description;
        this.max_amount = max_amount;
        this.min_amount = min_amount;
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
    
  
   
}
