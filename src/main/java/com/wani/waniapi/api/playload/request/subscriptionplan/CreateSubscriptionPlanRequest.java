package com.wani.waniapi.api.playload.request.subscriptionplan;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

public class CreateSubscriptionPlanRequest {
    @NotBlank
    private String name;
    private String description;

    private Integer min_amount;
    private Integer max_amount;
    private Integer frequency;
    private Integer roip;
    private Boolean available;
    private Integer duration;
    private LocalDateTime createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

 
  


    public Boolean getAvailable() {
        return available;
    }
    public void setAvailable(Boolean available) {
        this.available = available;
    }

	public Integer getMin_amount() {
		return min_amount;
	}

	public void setMin_amount(Integer min_amount) {
		this.min_amount = min_amount;
	}

	public Integer getMax_amount() {
		return max_amount;
	}

	public void setMax_amount(Integer max_amount) {
		this.max_amount = max_amount;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Integer getRoip() {
		return roip;
	}

	public void setRoip(Integer roip) {
		this.roip = roip;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

   

}
