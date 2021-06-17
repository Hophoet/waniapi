package com.wani.waniapi.api.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.Interest;
import com.wani.waniapi.api.models.InterestResponse;
import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.Subscription;
import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.playload.request.interest.CreateInterestRequest;
import com.wani.waniapi.api.playload.request.subscription.CreateSubscriptionRequest;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.InterestRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class InterestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;
    
    @Autowired
    InterestRepository interestRepository;


    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    PaymentRepository paymentRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    
    public InterestResponse payInterest(String subscriptionId) {
    	  // get the subscription
        Optional<Subscription> subscription =  subscriptionRepository.findById(
            subscriptionId
        );
        if(!subscription.isPresent()) {
        	   return null;
        }
        // get the subscription model object
        Subscription subscriptionValues = subscription.get();
        // get the subscription plan
        Optional<SubscriptionPlan> subscriptionPlan =  subscriptionPlanRepository.findById(
            subscriptionValues.getSubscriptionPlanId()
        );
        if(!subscriptionPlan.isPresent()) {
        	 return null;
        }
        // get the subscription plan model object
        SubscriptionPlan subscriptionPlanValues = subscriptionPlan.get();
        // get the user account if already exist or create if not
        Optional<Account> account =  accountRepository.findByUserId(
        		subscriptionValues.getUserId());
        Account accountValues;
        if(account.isPresent()) {
        	accountValues = account.get();
        }
        else {
        	Account newAccount = new Account(
        			subscriptionValues.getUserId());
        	accountValues = accountRepository.save(newAccount);
        }
        
        // calculate de date before the interest creation
        long subscriptionLastPaymentAtMillis = subscriptionValues.getLastPaymentAt();
        long currentDateMillis = System.currentTimeMillis();
        
        
        
        // calculate the duration miliseconds(difference between the current and the create milliseconds
        long durationMillis = currentDateMillis - subscriptionLastPaymentAtMillis;
        
        // calculation the duration millisecond day, mounth
        long durationMillisDays = durationMillis / (1000L * 60 * 60 * 24);
        long durationMillisMonths = durationMillis / (1000L * 60 * 60 * 24 * 30);
        long time = durationMillisMonths;
        
        // variable to check if it was a payment or not
        boolean paid = false;
        while(durationMillisMonths > 0) {
        	// create the interest(calculate the interest, ..
        	int interestAmount = subscriptionValues.getAmount() * subscriptionPlanValues.getInterest() / 100;
        	Interest interest = new Interest(subscriptionValues.getId(), interestAmount);
        	interestRepository.save(interest);
        	
        	
        	// update the user account amount
        	accountValues.addAmount(interestAmount);
        	accountRepository.save(accountValues);
        	
        	// update the subscription remaining duration (-1)
        	subscriptionValues.setTimeRemaining(
        			subscriptionValues.getTimeRemaining() - 1
        	);
        	// update the subscription last payment attribute
        	subscriptionValues.setLastPaymentAt(System.currentTimeMillis());
        	subscriptionRepository.save(subscriptionValues);
        	
        	
        	
        	
        	// indicated that it was a payment
        	if(!paid) paid = true;
        	
        	// update the loop
        	--durationMillisMonths;
        }
        
        InterestResponse interestResponse = new InterestResponse();
        int interestAmount = subscriptionValues.getAmount() * subscriptionPlanValues.getInterest() / 100;

        interestResponse.setAmount(interestAmount);
        interestResponse.setSubscriptionId(
        		subscriptionValues.getId()
        );
        interestResponse.setTime(time);
        interestResponse.setTotalAmount((int)time * interestAmount);
        interestResponse.setPaid(paid);
        interestResponse.setLastPaymentAt(subscriptionValues.getLastPaymentAt());
        interestResponse.setRemainingDays(30 - durationMillisDays);
        
        
        // create the interest if the date is valid
        
        // desincrement the Durationremaining to the subscription
        
        
        return interestResponse;
    }

     
    @PostMapping("/interests/pay")
    @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity payInterests(){
    	// interests from the database
    	List<Subscription> subscriptions = subscriptionRepository.findAll();
    	// interests response
    	List<InterestResponse> interestResponses = new ArrayList<>();
    	
    	//loop on the interests getted from the database
    	for(Subscription subscription: subscriptions) {
    		  // get the subscription
	        
    		InterestResponse interestResponse = this.payInterest(
    				subscription.getId()
    		);
    		interestResponses.add(interestResponse);
    	}
        return ResponseEntity.ok(interestResponses);

    }
    
    @PostMapping("/interest/pay")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	 public ResponseEntity payInterest(
			 @AuthenticationPrincipal UserDetailsImpl userDetail,
	         @Valid @RequestBody CreateInterestRequest createSubscriptionRequest
	    ){
	        // get the subscription
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
	        // get the subscription model object
	        Subscription subscriptionValues = subscription.get();
	      
	        // get the subscription plan
	        Optional<SubscriptionPlan> subscriptionPlan =  subscriptionPlanRepository.findById(
	            subscriptionValues.getSubscriptionPlanId()
	        );
	        if(!subscriptionPlan.isPresent()) {
	        	 return ResponseEntity
	                       .badRequest()
	                       .body(
	                           new ErrorResponse(
	                                   404,
	                                   "subscription-plan/not-found",
	                                   "subscription plan not found"
	                           )
	              );
	        }
	        InterestResponse interestResponse = this.payInterest(
	        		subscriptionValues.getId()
    		);
	       
	        return ResponseEntity.ok(interestResponse);
	    }



}
