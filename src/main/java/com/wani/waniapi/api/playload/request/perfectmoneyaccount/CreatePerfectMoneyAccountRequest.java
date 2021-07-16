package com.wani.waniapi.api.playload.request.perfectmoneyaccount;

import javax.validation.constraints.NotBlank;

public class CreatePerfectMoneyAccountRequest {
	
	@NotBlank
	private String widthdrawalAccount;

	public String getWidthdrawalAccount() {
		return widthdrawalAccount;
	}

	public void setWidthdrawalAccount(String widthdrawalAccount) {
		this.widthdrawalAccount = widthdrawalAccount;
	}

	
}
