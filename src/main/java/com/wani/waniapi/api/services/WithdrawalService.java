package com.wani.waniapi.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.AccountResponse;
import com.wani.waniapi.api.models.PaymentMethodResponse;
import com.wani.waniapi.api.models.Withdrawal;
import com.wani.waniapi.api.models.WithdrawalResponse;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.WithdrawalRepository;
import com.wani.waniapi.auth.repository.UserRepository;

@Service
public class WithdrawalService {

	@Autowired
	private WithdrawalRepository withdrawalRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	private PaymentMethodService paymentMethodService;

	@Autowired
	private AccountService accountService;
	

	public WithdrawalResponse getRequestResponse(String withdrawalId) {
		WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
		Optional<Withdrawal> withdrawal =  withdrawalRepository.findById(withdrawalId);
        if(!withdrawal.isPresent()){
        	// 
        	return null;
        }
        Withdrawal withdrawalObject = withdrawal.get();
        withdrawalResponse.setId(withdrawalObject.getId());
        withdrawalResponse.setCreatedAt(withdrawalObject.getCreatedAt());
        withdrawalResponse.setAmount(withdrawalObject.getAmount());
        
        // set the account response
        AccountResponse account = accountService.getRequestResponse(withdrawalObject.getAccountId());
        withdrawalResponse.setAccount(account);
        
        // set the payment method
        PaymentMethodResponse paymentMethod = paymentMethodService.getRequestResponse(withdrawalObject.getPaymentMethodId());
        withdrawalResponse.setPaymentMethod(paymentMethod);

		return withdrawalResponse;
	}

}
