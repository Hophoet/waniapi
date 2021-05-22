package com.wani.waniapi.api.playload.response.subscription;

import javax.validation.constraints.NotBlank;

public class SubscriptionResponse {

    private String id;

    private String userId;

    private String subscriptionPlanId;

    private String paymentId;

    private  Integer createdAt;

    private  Integer endedAt;

    public SubscriptionResponse(
        String id, 
        String userId, 
        String subscriptionPlanId, 
        String paymentId, 
        Integer createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.subscriptionPlanId = subscriptionPlanId;
        this.paymentId = paymentId;
        this.createdAt = createdAt;
        this._setEndedAt();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(String subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Integer endedAt) {
        this.endedAt = endedAt;
    }

    public void _setEndedAt() {
        this.endedAt = this.createdAt + 350;
    }
}
