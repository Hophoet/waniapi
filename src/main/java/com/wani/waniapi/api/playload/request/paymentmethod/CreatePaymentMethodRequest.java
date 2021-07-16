package com.wani.waniapi.api.playload.request.paymentmethod;

import javax.validation.constraints.NotBlank;

public class CreatePaymentMethodRequest {

	@NotBlank
	private String name;
	
	@NotBlank
	private String code;

	@NotBlank
	private String description;
	
	private Boolean isActive;

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	
}
