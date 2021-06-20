package com.wani.waniapi.api.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.models.Withdrawal;
import com.wani.waniapi.api.playload.request.subscription.CreateSubscriptionRequest;
import com.wani.waniapi.api.playload.request.withdrawal.CreateWithdrawal;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.WithdrawalRepository;
import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/v1")
public class WithdrawalController {
	
    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    
    @Autowired
    AccountRepository accountRepository;

	
	/*
     * get all withdrawals 
     */
    @GetMapping("admin/withdrawals")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Withdrawal> getWithdrawals(
    ){
        List<Withdrawal> withdrawals = withdrawalRepository.findAll();
        return withdrawals;
    }
    
	/*
     * get user withdrawals 
     */
    @GetMapping("admin/user/withdrawals")
    @PreAuthorize("hasRole('USER')")
    public List<Withdrawal> getUserWithdrawals(
 	       @AuthenticationPrincipal UserDetailsImpl userDetail
    ){
        List<Withdrawal> withdrawals = withdrawalRepository.findByUserId(userDetail.getId());
        return withdrawals;
    }
    
    @PostMapping("/withdrawal/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity createWithdrawal(
           @Valid @RequestBody CreateWithdrawal createWithdrawal,
 	       @AuthenticationPrincipal UserDetailsImpl userDetail
    ){
    	
    	if(createWithdrawal.getAmount() != null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "withdrawal/amount-is-required",
                                "the withdrawal amount is required"
                        )
                    );
    	}
    	if(createWithdrawal.getPhoneNumber() != null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "withdrawal/phone-number-is-required",
                                "the withdrawal phone number is required"
                        )
                    );
    	}
    	if(createWithdrawal.getPaymentMethodId() != null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "withdrawal/payment-method-id-is-required",
                                "the withdrawal payment method id is required"
                        )
                    );
    	}
    	Optional<PaymentMethod> paymentMethod =  paymentMethodRepository.findById(
    			createWithdrawal.getPaymentMethodId()
    	);
    	if(!paymentMethod.isPresent()){
    		return ResponseEntity
              .badRequest()
              .body(
                  new ErrorResponse(
                      404,
                      "withdrawal/payment-method-not-found",
                      "invalid payment method id"
                  )
              );
    	}
    	Optional<Account> account =  accountRepository.findByUserId(
    			userDetail.getId());
    	if(!account.isPresent()) {
    		return ResponseEntity
    	              .badRequest()
    	              .body(
    	                  new ErrorResponse(
    	                      401,
    	                      "withdrawal/account-not-found",
    	                      "invalid user account not found"
    	                  )
    	              );
    	}
    	Account accountValues = account.get();
    	if(createWithdrawal.getAmount() > accountValues.getAmount()) {
    		return ResponseEntity
  	              .badRequest()
  	              .body(
  	                  new ErrorResponse(
  	                      401,
  	                      "withdrawal/not-enough-amount",
  	                      "not enough amount available for the withdrawal"
  	                  )
  	              );
    	}
    	
    	// create the withdrawal
    	Withdrawal withdrawal = new Withdrawal(
    			userDetail.getId(), 
    			createWithdrawal.getPhoneNumber(), 
    			createWithdrawal.getPaymentMethodId(), 
    			createWithdrawal.getAmount());
    	
    	Withdrawal createdWithdrawal = withdrawalRepository.save(withdrawal);
    	
  
    	// update the user account amount
    	accountValues.setAmount(
    			accountValues.getAmount() - createWithdrawal.getAmount()
    	);
    	
    	accountRepository.save(accountValues);
    	
    	 // response
        return ResponseEntity.ok(
        		createdWithdrawal
        );
    	
    	
       
        
    }

}
