package com.wani.waniapi.api.controllers;


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
import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.auth.models.User;

import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.api.playload.request.subscription.CreateSubscriptionRequest;

import com.wani.waniapi.api.playload.response.subscription.SubscriptionResponse;

import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
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
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    PaymentRepository paymentRepository;

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
        if(createSubscriptionRequest.getAmount() < subscriptionPlan.get().getAmount()) {
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
        // get the payment method
        Optional<PaymentMethod> paymentMethod =  paymentMethodRepository.findById(
            createSubscriptionRequest.getPaymentMethodId()
        );
        // check if the payment method plan exists
        if(!paymentMethod.isPresent()){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            404,
                            "payment-method/not-found",
                            "invalid payment method id"
                    )
                );
        }
        PaymentMethod paymentMethodObject = paymentMethod.get();
        if(!paymentMethodObject.isActive()) {
	       	 return ResponseEntity
	                 .badRequest()
	                 .body(
	                     new ErrorResponse(
	                             400,
	                             "payment-method/not-available",
	                             "payment method is not available"
	                     )
	                 );
        	
        }
        /** MUST GET THE AUTHENTICATED USER NOT BY THE USER ID */
        // get the user id
        Optional<User> user =  userRepository.findById(
            createSubscriptionRequest.getUserId()
        );
        // check if the user exists
        if(!user.isPresent()){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            404,
                            "user/not-found",
                            "invalid user id"
                    )
                );
        }
        User userObject = user.get();
      
        // create payment
        Payment payment = new Payment(
            userObject.getId(),
            paymentMethodObject.getId()
        );
        Payment paymentObject = paymentRepository.save(payment);
        
        //create subscription
        Subscription subscription = new Subscription(
            userObject.getId(),
            createSubscriptionRequest.getSubscriptionPlanId(),
            paymentObject.getId(),
            createSubscriptionRequest.getPhoneNumber()
        );
        // set the subscription endedAt value
        subscription.setEndedAt(
            subscriptionPlanValues.getDuration() + subscription.getCreatedAt()
        );    
        subscription.setAmount(
        		subscriptionPlanValues.getAmount()
        );
        // set the remaining duration
        subscription.setTimeRemaining(
        		subscriptionPlanValues.getDuration()
        );
        Subscription subscriptionObject =  subscriptionRepository.save(subscription);
        // response
        return ResponseEntity.ok(
            subscriptionObject
        );
    }



    @GetMapping("/user-subscriptions")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Subscription> getUserSubscriptions(
       @Valid @RequestPart(required = true) String userId
    ){
        
        /** MUST GET THE AUTHENTICATED USER NOT BY THE USER ID */
        // get the user id
        Optional<User> user =  userRepository.findById(
            userId
        );
        // get the use object
        User userObject = user.get();

        /**
         * TODO
         * subscription must be get by the userId on the subscriptionRepository
         */
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(
            userId
        );
        return subscriptions;
    }

   
    @PutMapping("/subscription/pay")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?>  paySubscription(
       @AuthenticationPrincipal UserDetailsImpl userDetail,
       @Valid @RequestPart(required = true) String subscriptionId,
       @Valid @RequestPart(required = true) String paymentMethodId,
       @Valid @RequestPart(required = true) String phoneNumber
    ){
    	 
        Optional<Subscription> subscription =  subscriptionRepository.findById(
            subscriptionId
        );
    	// check if the subscription exists
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
        Subscription subscriptionObject = subscription.get();
        String subscriptionPlanId = subscriptionObject.getSubscriptionPlanId();
        Optional<SubscriptionPlan> subscriptionPlan =  subscriptionPlanRepository.findById(
                subscriptionPlanId
        );
    	// check if the subscription subscription plan exists
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
    	Optional<PaymentMethod> paymentMethod =  paymentMethodRepository.findById(
                paymentMethodId
          );
    	// check if the payment method exist
    	if(!paymentMethod.isPresent()) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "payment-method/not-found",
                                "payment method not found"
                        )
                    );
    	}
    	
    	String userId = subscriptionObject.getUserId();
    	// check if is the authorized user(who make the subscription before)
    	if(!userId.equals(userDetail.getId())) {
    		 return ResponseEntity
                     .badRequest()
                     .body(
                         new ErrorResponse(
                                 401,
                                 "paid/not-authoried",
                                 "the subscription is not for the authenticated user"
                         )
                     );
    	}
    	// check if the subscription is not paid
    	if(subscriptionObject.getPaid()) {
    		 return ResponseEntity
                     .badRequest()
                     .body(
                         new ErrorResponse(
                                 401,
                                 "subscription/was-paid",
                                 "subscription already paid"
                         )
                     );
    	}
    	// Make the payment request, 
    
    	
    	
    	// Update the subscription payment phone number
    	subscriptionObject.setPhoneNumber(phoneNumber);
    	subscriptionObject.setPaymentId(paymentMethodId);
    	
       return ResponseEntity.ok(subscriptionObject);
    }

}