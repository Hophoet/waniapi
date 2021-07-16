package com.wani.waniapi.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.PaymentMethodResponse;
import com.wani.waniapi.api.models.PaymentResponse;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.services.UserService;


@Service
public class PaymentMethodService {
	
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;

	
	public PaymentMethodResponse getRequestResponse(String paymentMethodId) {
		PaymentMethodResponse paymentMethodResponse = new PaymentMethodResponse();
		Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(paymentMethodId);
		if(!paymentMethod.isPresent()) {
			return null;
		}
		PaymentMethod paymentMethodObject = paymentMethod.get();
		paymentMethodResponse.setId(paymentMethodObject.getId());
		paymentMethodResponse.setName(paymentMethodObject.getName());
		paymentMethodResponse.setCode(paymentMethodObject.getCode());
		paymentMethodResponse.setDescription(paymentMethodObject.getDescription());
		paymentMethodResponse.setActive(paymentMethodObject.isActive());
		return paymentMethodResponse;
	}
}
