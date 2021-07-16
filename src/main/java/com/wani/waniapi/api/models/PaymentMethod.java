package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "paymentMethods")
public class PaymentMethod {
    @Id
    private String id;
    private String name;
    private String code;
    private String description;
    private boolean isActive;


    public PaymentMethod(
        String name, 
        String code,
        String description
    ) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.setActive(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
  
}
