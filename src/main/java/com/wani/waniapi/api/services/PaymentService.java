package com.wani.waniapi.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.PaymentResponse;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.services.UserService;


@Service
public class PaymentService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@Autowired
	private PaymentRepository paymentRepository;

	
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;
	
	public PaymentResponse getRequestResponse(String paymentId) {
		PaymentResponse paymentResponse = new PaymentResponse();
		Optional<Payment> payment = paymentRepository.findById(paymentId);
		if(!payment.isPresent()) {
			return null;
		}
		Payment paymentObject = payment.get();
		paymentResponse.setId(paymentObject.getId());
		paymentResponse.setCreatedAt(paymentObject.getCreatedAt());
		paymentResponse.setAmount(paymentObject.getAmount());
		paymentResponse.setAccount(
			accountService.getRequestResponse(paymentObject.getAccountId())	
				);		
		try {
			Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(paymentObject.getPaymentMethodId());
			if(paymentMethod.isPresent()) {
				paymentResponse.setPaymentMethod(
						paymentMethodService.getRequestResponse(paymentMethod.get().getId())
				);
						
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			return paymentResponse;
		}
		return paymentResponse;
	}
}
