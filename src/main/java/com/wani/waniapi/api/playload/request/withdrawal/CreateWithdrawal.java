package com.wani.waniapi.api.playload.request.withdrawal;

import javax.validation.constraints.NotBlank;

public class CreateWithdrawal {

	    @NotBlank
	    private String paymentMethodId;
	
	    private Integer amount;
	    
	    private String accountId;

		public String getPaymentMethodId() {
			return paymentMethodId;
		}

		public void setPaymentMethodId(String paymentMethodId) {
			this.paymentMethodId = paymentMethodId;
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
