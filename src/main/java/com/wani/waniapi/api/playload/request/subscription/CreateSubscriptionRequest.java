package com.wani.waniapi.api.playload.request.subscription;

import javax.validation.constraints.NotBlank;

public class CreateSubscriptionRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String subscriptionPlanId;

    @NotBlank
    private String paymentMethodId;
    
    @NotBlank
    private String phoneNumber;

    private  Integer createdAt;

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

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getPhoneNumber() {
		return phoneNumber;
	}
    
    public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
