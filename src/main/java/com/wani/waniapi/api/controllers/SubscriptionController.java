package com.wani.waniapi.api.controllers;

import com.wani.waniapi.api.models.SubscriptionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.models.Subscription;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.auth.models.User;

import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.api.playload.request.subscription.CreateSubscriptionRequest;


import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.api.services.PerfectMoneyService;
import com.wani.waniapi.api.services.SubscriptionService;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.PerfectMoneyAccountRepository;
import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;
import com.wani.waniapi.api.playload.request.subscription.CreateSubscriptionRequest;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SubscriptionController {
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    PerfectMoneyService perfectMoneyService;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    PaymentRepository paymentRepository;
    
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/subscription/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity createSubscription(
           @Valid @RequestBody CreateSubscriptionRequest createSubscriptionRequest
    ){
        // get the subscription plan
        Optional<SubscriptionPlan> subscriptionPlan =  subscriptionPlanRepository.findById(
            createSubscriptionRequest.getSubscriptionPlanId()
        );
        // check if the subscription plan exists
        if(!subscriptionPlan.isPresent()){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            404,
                            "subscription-plan/not-found",
                            "invalid subscription plan id"
                    )
                );
        }
        // get the suscription plan object
        SubscriptionPlan subscriptionPlanValues = subscriptionPlan.get();
        // check the availability of the subscription plan
        if(subscriptionPlanValues.getAvailable() == null || subscriptionPlanValues.getAvailable() == false) {
	       	 return ResponseEntity
	                 .badRequest()
	                 .body(
	                     new ErrorResponse(
	                             400,
	                             "subscription-plan/not-available",
	                             "subscription plan is not available"
	                     )
	                 );
        }
        if(createSubscriptionRequest.getAmount() == null) {
        	 return ResponseEntity
                     .badRequest()
                     .body(
                         new ErrorResponse(
                                 400,
                                 "subscription/amount-not-set",
                                 "subscription amount must be set"
                         )
                     );
        }
        if(createSubscriptionRequest.getAmount() < subscriptionPlan.get().getMinAmount()) {
        	 return ResponseEntity
                     .badRequest()
                     .body(
                         new ErrorResponse(
                                 400,
                                 "subscription/amount-not-enough",
                                 "subscription amount not reach the minimum amount"
                         )
                     );
        } 
        if(createSubscriptionRequest.getAmount() > subscriptionPlan.get().getMaxAmount()) {
       	 return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                400,
                                "subscription/amount-not-exceed",
                                "subscription amount not reach the maximum amount for this subscription plan"
                        )
                    );
       }
        
        /** MUST GET THE AUTHENTICATED USER NOT BY THE USER ID */
        // get the payment method 
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(createSubscriptionRequest.getPaymentMethodId());
        if(!paymentMethod.isPresent()) {
       	 	return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                400,
                                "payment-method/not-found",
                                "payment method not found"
                        )
                    );
        }
        
        // get the payment method object
        PaymentMethod paymentMethodObject = paymentMethod.get(); 
        if(paymentMethodObject.getCode().contentEquals("PM")){
        	String perfectMoneyAccountId =  createSubscriptionRequest.getPerfectMoneyAccountId();
        	String perfectMoneyPassPhrase =  createSubscriptionRequest.getPerfectMoneyPassPhrase();
        	String perfectMoneyAccount =  createSubscriptionRequest.getPerfectMoneyAccount();
        	if(perfectMoneyAccountId == null) {
					return ResponseEntity
						.badRequest()
						.body(
							new ErrorResponse(
									400,
									"perfect-money/account-id-required",
									"perfect money account id is required for perfect money payment"
							)
						);
				}
        	if(perfectMoneyPassPhrase == null) {
				return ResponseEntity
					.badRequest()
					.body(
						new ErrorResponse(
								400,
								"perfect-money/pass-phrase-required",
								"perfect money pass phrase is required for perfect money payment"
						)
					);
        		
        	}
        	if(perfectMoneyAccount == null) {
				return ResponseEntity
					.badRequest()
					.body(
						new ErrorResponse(
								400,
								"perfect-money/account-required",
								"perfect money account is required for perfect money payment"
						)
					);
        	}
        }
        else {
			return ResponseEntity
				.badRequest()
				.body(
					new ErrorResponse(
							400,
							"payment-method/not-available",
							"payment method is not avialable"
					)
				);
        	
        }
        
		/* TODO 
		 * Get pefect money information from the env
		 * Make the perfect money api payment
		 * 
		 * 
		 * 
		 */
		
		// get perfect money deposit account
		String PERFECT_MONEY_DEPOSIT_ACCOUNT = perfectMoneyService.getPERFECT_MONEY_DEPOSIT_ACCOUNT();
        	
    

        
        // get the user account id
        Optional<Account> account = accountRepository.findById(createSubscriptionRequest.getAccountId());
        // check if the user accoun texists
        if(!account.isPresent()){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            404,
                            "user-account/not-found",
                            "user account not found"
                    )
                );
        }
        Account accountObject = account.get();
        
        
        // create payment
        Payment payment = new Payment(
        		accountObject.getId(), 
        		createSubscriptionRequest.getAmount()
        );
        // set the payment id
        payment.setPaymentMethodId(paymentMethodObject.getId());
        
        Payment paymentObject = paymentRepository.save(payment);
        
        //create subscription
        Subscription subscription = new Subscription(
            accountObject.getId(),
            createSubscriptionRequest.getAmount(),
            createSubscriptionRequest.getSubscriptionPlanId(),
            paymentObject.getId()
        );
        // set the subscription endedAt value
        subscription.setEndedAt(
        		subscription.getCreatedAt().plusDays(subscriptionPlanValues.getDuration())
       );
        
        // set the next interest payment at 
        subscription.setNextInterestPaymentAt(

        		subscription.getCreatedAt().plusDays(subscriptionPlanValues.getFrequency())
        );
        // set the remaining duration
     
        Subscription subscriptionObject =  subscriptionRepository.save(subscription);
        // response
        return ResponseEntity.ok(
            subscriptionObject
        );
    }



    @GetMapping("/account-subscriptions")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Subscription> getAccountSubscriptions(
       @Valid @RequestPart(required = true) String accountId
    ){
        
        /** MUST GET THE AUTHENTICATED USER NOT BY THE USER ID */
        // get the account
        Optional<Account> account =  accountRepository.findById(accountId);
        
        // get the account object
        Account accountObject = account.get();

        /**
         * TODO
         * subscription must be get by the userId on the subscriptionRepository
         */
        List<Subscription> subscriptions = subscriptionRepository.findByAccountId(accountId);
        return subscriptions;
    }
    
    @GetMapping("/subscription/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getSubscription(
        @PathVariable String id
    ){
        // get the subscription
        Optional<Subscription> subscription =  subscriptionRepository.findById(id);
        // check if the subcription exists
        if(!subscription.isPresent()){
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
        
        SubscriptionResponse subscriptionResponse = subscriptionService.getRequestResponse(id);
    
        return ResponseEntity.ok(subscriptionResponse);
    }

   
//    @PutMapping("/subscription/pay")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<?>  paySubscription(
//       @AuthenticationPrincipal UserDetailsImpl userDetail,
//       @Valid @RequestPart(required = true) String subscriptionId,
//       @Valid @RequestPart(required = true) String paymentMethodId,
//       @Valid @RequestPart(required = true) String phoneNumber
//    ){
//    	 
//        Optional<Subscription> subscription =  subscriptionRepository.findById(
//            subscriptionId
//        );
//    	// check if the subscription exists
//        if(!subscription.isPresent()) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(
//                        new ErrorResponse(
//                                404,
//                                "subscription/not-found",
//                                "subscription not found"
//                        )
//                    );
//        }
//        Subscription subscriptionObject = subscription.get();
//        String subscriptionPlanId = subscriptionObject.getSubscriptionPlanId();
//        Optional<SubscriptionPlan> subscriptionPlan =  subscriptionPlanRepository.findById(
//                subscriptionPlanId
//        );
//    	// check if the subscription subscription plan exists
//    	if(!subscriptionPlan.isPresent()) {
//    		 return ResponseEntity
//                     .badRequest()
//                     .body(
//                         new ErrorResponse(
//                                 404,
//                                 "subscription-plan/not-found",
//                                 "subscription plan not found"
//                         )
//                     );
//    	}
//    	Optional<PaymentMethod> paymentMethod =  paymentMethodRepository.findById(
//                paymentMethodId
//          );
//    	// check if the payment method exist
//    	if(!paymentMethod.isPresent()) {
//    		return ResponseEntity
//                    .badRequest()
//                    .body(
//                        new ErrorResponse(
//                                404,
//                                "payment-method/not-found",
//                                "payment method not found"
//                        )
//                    );
//    	}
//    	
//    	String userId = subscriptionObject.getUserId();
//    	// check if is the authorized user(who make the subscription before)
//    	if(!userId.equals(userDetail.getId())) {
//    		 return ResponseEntity
//                     .badRequest()
//                     .body(
//                         new ErrorResponse(
//                                 401,
//                                 "paid/not-authoried",
//                                 "the subscription is not for the authenticated user"
//                         )
//                     );
//    	}
//    	// check if the subscription is not paid
//    	if(subscriptionObject.getPaid()) {
//    		 return ResponseEntity
//                     .badRequest()
//                     .body(
//                         new ErrorResponse(
//                                 401,
//                                 "subscription/was-paid",
//                                 "subscription already paid"
//                         )
//                     );
//    	}
//    	// Make the payment request, 
//    
//    	
//    	
//    	// Update the subscription payment phone number
//    	subscriptionObject.setPhoneNumber(phoneNumber);
//    	subscriptionObject.setPaymentId(paymentMethodId);
//    	
//       return ResponseEntity.ok(subscriptionObject);
//    }

}