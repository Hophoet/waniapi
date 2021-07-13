package com.wani.waniapi.api.services;

import java.time.*;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.AccountResponse;
import com.wani.waniapi.api.models.Interest;
import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.Subscription;
import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.models.SubscriptionResponse;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.InterestRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.repository.UserRepository;

@Service
public class InterestService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private SubscriptionPlanService subscriptionPlanService;

	@Autowired
	private PaymentRepository paymentReposity;

	@Autowired
	private InterestRepository interestRepository;
	
	
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private SubscriptionPlanRepository subscriptionPlanRepository;
	
	
	@Autowired
	private AccountService accountService;
	
	@Autowired 
	private SubscriptionRepository subscriptionRepository;
	
	public Subscription paySubscriptionInterest(String subscriptionId) {
		SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
		// check if the subscription exists
		Optional<Subscription> subscription =  subscriptionRepository.findById(subscriptionId);
        if(!subscription.isPresent()){
        	// get the user
        	return null;
        }
        Subscription subscriptionObject = subscription.get();
		// check if the subscription plan exists
        Optional<SubscriptionPlan> subscriptionPlan = subscriptionPlanRepository.findById(
        		subscriptionObject.getSubscriptionPlanId());
       if(!subscriptionPlan.isPresent()) {
    	   return null;
       }
       SubscriptionPlan subscriptionPlanObject = subscriptionPlan.get();
       
       
       // check if the user account exists
        Optional<Account> account = accountRepository.findById(
        		subscriptionObject.getAccountId());
       if(!account.isPresent()) {
    	   return null;
       }
       Account accountObject = account.get();
       

       // check if the subscription not finish
       if( subscriptionObject.getEndedAt().isBefore(LocalDateTime.now()) ) {
    	   // the subscription is finish in this case
    	   return null;
       }
       
       
       // get how many time the subcription can be take
       Duration duration = Duration.between(
    		   subscriptionObject.getNextInterestPaymentAt(), 
    		   LocalDateTime.now());
       long durationDays = duration.toDays();
       // get the number of time the interest payment can be done
       long interestPaymentTime = durationDays / subscriptionPlanObject.getFrequency();
       
       // check if the subcription reach the minimum time for the interest payment
       if(interestPaymentTime < 1) {
    	   // the interestPaymentTime must be at least equal or greater than 1 to be able to get the 
    	   // interest payment
    	   return null;
       }
       
       // calculate the interest payment amount
       int interestPaymentAmount =(int) (
    		   (subscriptionObject.getAmount() * subscriptionPlanObject.getRoip())/100 * interestPaymentTime
    		   );

       // create the interest payment
       Interest interest = new Interest(
    		   subscriptionObject.getId(), 
    		   subscriptionObject.getAccountId(), 
    		   interestPaymentAmount
    	);
       interestRepository.save(interest);
       
       // update the user amount
       accountObject.addAmount(interestPaymentAmount);
       accountRepository.save(accountObject);
       
       // update the subscription 
       subscriptionObject.setNextInterestPaymentAt(
    		   subscriptionObject.getNextInterestPaymentAt().plusDays(
    				   subscriptionPlanObject.getDuration() * interestPaymentTime)
    	);
       Subscription updatedSubscription = subscriptionRepository.save(subscriptionObject);
       
        return updatedSubscription;
	}
	
}