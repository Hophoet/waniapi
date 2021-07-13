package com.wani.waniapi.api.models;


import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String accountId;
    private String paymentMethodId;
    private LocalDateTime createdAt;
    private Integer amount;

    public Payment(
        String accountId, 
        Integer amount,
        String paymentMethodId
    ) {
        this.setAccountId(accountId);
        this.paymentMethodId = paymentMethodId;
        this.setAmount(amount);
    	this.setCreatedAt(LocalDateTime.now());
    }
    

    public Payment(
        String accountId, 
        Integer amount
    ) {
        this.setAccountId(accountId);
        this.setAmount(amount);
    	this.setCreatedAt(LocalDateTime.now());
    	// must set the default payment
    }
    
    public Payment() {
    	// 
    	this.setCreatedAt(LocalDateTime.now());

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

 

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }


	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public PaymentResponse getRequestResponse() {
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setId(this.id);
		paymentResponse.setCreatedAt(this.createdAt);
		
		
		return paymentResponse;
	}


}
