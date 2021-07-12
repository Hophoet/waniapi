package com.wani.waniapi.api.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.Deposit;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.playload.request.deposit.CreateDeposit;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.DepositRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;



@RestController
@RequestMapping("/api/v1")
public class DepositController {


    @Autowired
    DepositRepository depositRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    @PostMapping("/deposit/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity createDeposit(
           @Valid @RequestBody CreateDeposit createDeposit,
 	       @AuthenticationPrincipal UserDetailsImpl userDetail
    ){
    	
    	if(createDeposit.getAmount() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "deposit/amount-is-required",
                                "the deposit amount is required"
                        )
                    );
    	}
   
    	if(createDeposit.getPaymentMethodId() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "deposit/payment-method-id-is-required",
                                "the deposit payment method id is required"
                        )
                    );
    	}
    	Optional<PaymentMethod> paymentMethod =  paymentMethodRepository.findById(
    			createDeposit.getPaymentMethodId()
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
    	Optional<Account> account =  accountRepository.findById(createDeposit.getAccountId());
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

    	
    	// create the deposit
    	Deposit deposit = new Deposit(
    			createDeposit.getAmount(), 
    			createDeposit.getAccountId(), 
    			createDeposit.getPaymentMethodId());
    	
    	Deposit createdDeposit = depositRepository.save(deposit);
    	
    	
    	// add the deposit amount to the account
    	accountValues.addAmount(createdDeposit.getAmount());
    	
    	accountRepository.save(accountValues);
    	
    	 // response
        return ResponseEntity.ok(
        		createdDeposit
        );
    	
    	
       
        
    }

}
