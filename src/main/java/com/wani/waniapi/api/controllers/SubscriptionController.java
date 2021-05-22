package com.wani.waniapi.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.models.Subscription;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.auth.models.User;

import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.api.playload.request.subscription.CreateSubscriptionRequest;

import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.auth.repository.UserRepository;

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
    @PreAuthorize("hasRole('ADMIN')")
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
        PaymentMethod paymentMethodObject = paymentMethod.get();
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
            paymentObject.getId()
        );


        return ResponseEntity.ok(
            subscriptionRepository.save(subscription)
        );
    }

}