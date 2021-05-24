package com.wani.waniapi.api.playload.response.subscription;

import javax.validation.constraints.NotBlank;

public class SubscriptionResponse {

    private String id;

    private String userId;

    private String subscriptionPlanId;

    private String paymentId;

    private  long createdAt;

    private  long endedAt;

    public SubscriptionResponse(
        String id, 
        String userId, 
        String subscriptionPlanId, 
        String paymentId, 
        long createdAt
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }

    public void _setEndedAt() {
        this.endedAt = this.createdAt + 350;
    }
}
