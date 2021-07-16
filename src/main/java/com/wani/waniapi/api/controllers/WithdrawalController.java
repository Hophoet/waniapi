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
import com.wani.waniapi.api.models.PerfectMoneyAccount;
import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.models.Withdrawal;
import com.wani.waniapi.api.playload.request.subscription.CreateSubscriptionRequest;
import com.wani.waniapi.api.playload.request.withdrawal.CreateWithdrawal;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.repositories.PerfectMoneyAccountRepository;
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

    @Autowired
    PerfectMoneyAccountRepository perfectMonayAccountRepository;
	
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
//    @GetMapping("admin/user/withdrawals")
//    @PreAuthorize("hasRole('USER')")
//    public List<Withdrawal> getUserAccountWithdrawals(
// 	       @AuthenticationPrincipal UserDetailsImpl userDetail
//    ){
//        List<Withdrawal> withdrawals = withdrawalRepository.findAccountId(accountId)(userDetail.getId());
//        return withdrawals;
//    }
    
    
    @PostMapping("/withdrawal/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity createWithdrawal(
           @Valid @RequestBody CreateWithdrawal createWithdrawal,
 	       @AuthenticationPrincipal UserDetailsImpl userDetail
    ){
    	
    	if(createWithdrawal.getAmount() == null) {
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
   
    	if(createWithdrawal.getPaymentMethodId() == null) {
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
    	Optional<Account> account =  accountRepository.findById(createWithdrawal.getAccountId());
    	if(!account.isPresent()) {
    		return ResponseEntity
    	              .badRequest()
    	              .body(
    	                  new ErrorResponse(
    	                      404,
    	                      "withdrawal/account-not-found",
    	                      "invalid user account not found"
    	                  )
    	              );
    	}
    	Account accountValues = account.get();

    	if(!accountValues.getUserId().contentEquals(userDetail.getId())) {
    		return ResponseEntity
  	              .badRequest()
  	              .body(
  	                  new ErrorResponse(
  	                      401,
  	                      "withdrawal/account-access-not-authorized",
  	                      "withdrawal account access is not authorized"
  	                  )
  	              );
    	}
    	
    	
    	if(createWithdrawal.getAmount() > accountValues.getAmount()) {
    		return ResponseEntity
  	              .badRequest()
  	              .body(
  	                  new ErrorResponse(
  	                      400,
  	                      "withdrawal/not-enough-amount",
  	                      "not enough amount available for the withdrawal"
  	                  )
  	              );
    	}
    	// check payment method validataion
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
    	
        // get the payment method object
        PaymentMethod paymentMethodObject = paymentMethod.get(); 
        if(!paymentMethodObject.getCode().contentEquals("PM")){
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
        
        // check if the user have a perfect money account
		// create new account object
		Optional<PerfectMoneyAccount> perfectMoneyAccount = perfectMonayAccountRepository.findByUserId(userDetail.getId());
		if(!perfectMoneyAccount.isPresent()) {
			return ResponseEntity
				.badRequest()
				.body(
					new ErrorResponse(
							400,
							"perfect-money-account/not-set",
							"perfect-money account must be found"
					)
				);
		}

        //TODO Make the perfect money api withdrawal payment to the user account
    	
    	
    	// create the withdrawal
    	Withdrawal withdrawal = new Withdrawal(
    			accountValues.getId(), 
    			paymentMethodObject.getId(), 
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
