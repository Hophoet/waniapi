package com.wani.waniapi.api.playload.request.withdrawal;

import javax.validation.constraints.NotBlank;

public class CreateWithdrawal {

	    @NotBlank
	    private String paymentMethodId;
	    
	    @NotBlank
	    private String phoneNumber;

	    private Integer amount;

		public String getPaymentMethodId() {
			return paymentMethodId;
		}

		public void setPaymentMethodId(String paymentMethodId) {
			this.paymentMethodId = paymentMethodId;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public Integer getAmount() {
			return amount;
		}

		public void setAmount(Integer amount) {
			this.amount = amount;
		}







}
