package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subscriptionPlans")
public class SubscriptionPlan {
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

    public SubscriptionPlan(){

    }

    public SubscriptionPlan(
        String name, 
        String description, 
        Integer amount,
        Integer interest,
        Integer duration
    ) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.interest = interest;
        this.duration = duration;
        this.createdAt = 20390;
        this.available = true;
        this.subscriptionsCount = 0;
    }
    public SubscriptionPlan(
        String name, 
        String description, 
        Integer amount,
        Integer interest,
        Integer duration,
        Boolean available
    ) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.interest = interest;
        this.duration = duration;
        this.createdAt = 20390;
        this.available = available;
        this.subscriptionsCount = 0;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getAvailable() {
        return available;
    }
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getInterest() {
        return interest;
    }
    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    public Integer getSubscriptionsCount() {
        return subscriptionsCount;
    }
    public void setSubscriptionsCount(Integer sC) {
        this.subscriptionsCount = sC;
    }

}
