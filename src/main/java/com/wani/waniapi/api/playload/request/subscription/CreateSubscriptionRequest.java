package com.wani.waniapi.api.playload.request.subscription;

import javax.validation.constraints.NotBlank;

public class CreateSubscriptionRequest {
    @NotBlank
    private String accountId;

    @NotBlank
    private String subscriptionPlanId;


    private Integer amount;
    

    private  Integer createdAt;



    public String getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(String subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

   

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }
    


	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

    


}
