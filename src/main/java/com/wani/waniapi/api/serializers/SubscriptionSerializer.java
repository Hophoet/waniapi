package com.wani.waniapi.api.serializers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.Subscription;
import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.models.SubscriptionResponse;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.repository.UserRepository;


public class SubscriptionSerializer {
	

    static UserRepository userRepository;
    
    @Autowired
    static PaymentMethodRepository paymentMethodRepository;

    @Autowired
    static SubscriptionPlanRepository subscriptionPlanRepository;


	public static SubscriptionResponse serializer(Subscription subscription) {
		SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
//		System.out.println(subscription.getUserId());
//		Optional<User> user = userRepository.findById("60525b8b0344131089c5fa66");
//		if(user.isPresent()) {
////			subscriptionResponse.setUser(user.get());
//		}
//		Optional<SubscriptionPlan> subscriptionPlan = subscriptionPlanRepository.findById(subscription.getSubscriptionPlanId());
//		if(subscriptionPlan.isPresent()) {
////			subscriptionResponse.setSubscriptionPlan(subscriptionPlan.get());
//		}
//		Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(subscription.getPaymentId());
//		if(paymentMethod.isPresent()) {
////			subscriptionResponse.setPaymentMethod(paymentMethod.get());
//		}
		
		return subscriptionResponse;
	}

}
