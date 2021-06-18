package com.wani.waniapi.api.controllers;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.Subscription;
import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.playload.request.interest.CreateInterestRequest;
import com.wani.waniapi.api.playload.request.subscription.CreateSubscriptionRequest;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.repository.UserRepository;

public class InterestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    PaymentRepository paymentRepository;

	 public ResponseEntity createInterest(
	           @Valid @RequestBody CreateInterestRequest createSubscriptionRequest
	    ){
	        // get the subscription plan
	        Optional<Subscription> subscription =  subscriptionRepository.findById(
	            createSubscriptionRequest.getSubscriptionId()
	        );
	        if(!subscription.isPresent()) {
	        	   return ResponseEntity
	                       .badRequest()
	                       .body(
	                           new ErrorResponse(
	                                   404,
	                                   "subscription/not-found",
	                                   "subscription not found"
	                           )
	                       );
	        	
	        }
	        Subscription subscriptionValues = subscription.get();
	        // calculate de date before the interest creation
	        long subscriptionCreatedAt = subscriptionValues.getCreatedAt();
	        long currentDate = new Date().getTime();
	        
	        // create the interest if the date is valid
	        
	        // desincrement the Durationremaining to the subscription
	        
	        
	        
	        // check if the subscription plan exists
	        return null;
	    }



}
