package com.wani.waniapi.api.services;

import java.time.*;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.AccountResponse;
import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.Subscription;
import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.models.SubscriptionResponse;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.repository.UserRepository;

@Service
public class SubscriptionService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private SubscriptionPlanService subscriptionPlanService;

	@Autowired
	private PaymentRepository paymentReposity;
	
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private SubscriptionPlanRepository subscriptionPlanRepository;
	
	
	@Autowired
	private AccountService accountService;
	
	@Autowired 
	private SubscriptionRepository subscriptionRepository;
	
	public SubscriptionResponse getRequestResponse( String subcriptionId) {
		SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
	
		Optional<Subscription> subscription =  subscriptionRepository.findById(subcriptionId);
        if(!subscription.isPresent()){
        	// get the user
        	return null;
        }
        Subscription subscriptionObject = subscription.get();
        subscriptionResponse.setId(subscriptionObject.getId());
        subscriptionResponse.setAmount(subscriptionObject.getAmount());
        subscriptionResponse.setCreatedAt(subscriptionObject.getCreatedAt());
        subscriptionResponse.setEndedAt(subscriptionObject.getEndedAt());
        subscriptionResponse.setLastInterestPaymentAt(subscriptionObject.getLastInterestPaymentAt());
        subscriptionResponse.setNextInterestPaymentAt(subscriptionObject.getNextInterestPaymentAt());
        subscriptionResponse.setPaid(subscriptionObject.getPaid());
        subscriptionResponse.setTimeRemaining(subscriptionObject.getTimeRemaining());
        subscriptionResponse.setAccount(
        		accountService.getRequestResponse(subscriptionObject.getAccountId())
        		
        );
        
       // set the subcription plan response object 
		subscriptionResponse.setSubscriptionPlan(
				subscriptionPlanService.getRequestResponse(
					subscriptionObject.getSubscriptionPlanId()	
		));

        Optional<Payment> payment =  paymentReposity.findById(
        		subscriptionObject.getPaymentId()
        		);
        if(payment.isPresent()) {
			// set payment response 
			subscriptionResponse.setPayment(
				paymentService.getRequestResponse(
						subscriptionObject.getPaymentId())
					);
        	
        }
        
        
//        subscriptionResponse.setAccount(subscriptionObject.getA);
//    	accountResponse.setId(accountObject.getId());
//		accountResponse.setAmount(accountObject.getAmount());
//		accountResponse.setCreatedAt(accountObject.getCreatedAt());
//    	// get the user 
//    	Optional<User> user = userRepository.findById(accountObject.getUserId());
//    	if(user.isPresent()) {
//    		// set the user response to the account response
//    		accountResponse.setUser(
//    				user.get().getRequestResponse()
//    		);
//    	}

        return subscriptionResponse;
	}
}