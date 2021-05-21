package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SubscriptionPlan {
    @Id
    private String id;
    private String name;
    private String description;
    private Integer amount;
    private Integer createdAt;


    public SubscriptionPlan(String name, String description, Integer amount) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.createdAt = 20390;
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

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

}
