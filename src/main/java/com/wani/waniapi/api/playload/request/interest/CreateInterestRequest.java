package com.wani.waniapi.api.playload.request.interest;

import javax.validation.constraints.NotBlank;

public class CreateInterestRequest {
	@NotBlank
    private String subscriptionId;

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}


}
