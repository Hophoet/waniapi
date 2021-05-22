package com.wani.waniapi.api.playload.request.subscriptionplan;

import javax.validation.constraints.NotBlank;

public class CreateSubscriptionPlanRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    // @NotBlank
    private  Integer amount;

    // @NotBlank
    private  Integer duration;

    private  Integer createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
