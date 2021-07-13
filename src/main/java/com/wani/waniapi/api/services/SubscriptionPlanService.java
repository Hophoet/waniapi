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
import com.wani.waniapi.api.models.SubscriptionPlanResponse;
import com.wani.waniapi.api.models.SubscriptionResponse;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.repository.UserRepository;

@Service
public class SubscriptionPlanService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;

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
	
	public SubscriptionPlanResponse getRequestResponse( String subcriptionPlanId) {
		SubscriptionPlanResponse subscriptionPlanResponse = new SubscriptionPlanResponse();
	
		Optional<SubscriptionPlan> subscriptionPlan =  subscriptionPlanRepository.findById(subcriptionPlanId);
        if(!subscriptionPlan.isPresent()){
        	// 
        	return null;
        }
        SubscriptionPlan subscriptionPlanObject = subscriptionPlan.get();
        subscriptionPlanResponse.setId(subscriptionPlanObject.getId());
        subscriptionPlanResponse.setName(subscriptionPlanObject.getName());
        subscriptionPlanResponse.setDescription(subscriptionPlanObject.getDescription());
        subscriptionPlanResponse.setAvailable(subscriptionPlanObject.getAvailable());
        subscriptionPlanResponse.setDuration(subscriptionPlanObject.getDuration());
        subscriptionPlanResponse.setMinAmount(subscriptionPlanObject.getMinAmount());
        subscriptionPlanResponse.setMaxAmount(subscriptionPlanObject.getMaxAmount());
        subscriptionPlanResponse.setFrequency(subscriptionPlanObject.getFrequency());
        subscriptionPlanResponse.setRoip(subscriptionPlanObject.getRoip());
        subscriptionPlanResponse.setCreatedAt(subscriptionPlanObject.getCreatedAt());
        
        // set subcriptions count
        int subscriptionsCount = 0;
        List<Subscription> subscriptions = subscriptionRepository.findBySubscriptionPlanId(subscriptionPlanObject.getId());
        subscriptionPlanResponse.setSubscriptionsCount(
        	subscriptions.size()
        );
        
        return subscriptionPlanResponse;
	}
}