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
import com.wani.waniapi.api.services.PerfectMoneyService;
import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@RestController
@RequestMapping("/api/v1")
public class WithdrawalController {
	
    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    
    @Autowired
    PerfectMoneyService perfectMoneyService;

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
		// get perfect money object
		PerfectMoneyAccount perfectMoneyAccoutObject = perfectMoneyAccount.get();

		
		
		/* TODO 
		 * Get pefect money information from the env
		 * Make the perfect money api payment
		 * 
		 * 
		 * 
		 */
		
		// get perfect money deposit account
		String PERFECT_MONEY_DEPOSIT_ACCOUNT = perfectMoneyService.getPERFECT_MONEY_DEPOSIT_ACCOUNT();
		String PERFECT_MONEY_ACCOUNT_ID = perfectMoneyService.getPERFECT_MONEY_ACCOUNT_ID();
		String PERFECT_MONEY_PASS_PRASE = perfectMoneyService.getPERFECT_MONEY_PASS_PHRASE();
		String PERFECT_MONEY_WITHDRAWAL_ACCOUNT = perfectMoneyService.getPERFECT_MONEY_WITHDRAWAL_ACCOUNT();
		// message the payment du suscription 
		String subscriptionPaymentMemo = "withdrawal "+createWithdrawal.getAmount()+ " by "+userDetail.getUsername();
		// make the perfect money payment
		String perfectMoneyPaymentResponseBodyString;
		// get the payment status code
		int perfectMoneyPaymentResponseStatus;
		try {
			HttpResponse perfectMoneyPaymentResponse = Unirest.post("https://perfectmoney.is/acct/confirm.asp")
			.field("AccountID", PERFECT_MONEY_ACCOUNT_ID)
			.field("PassPhrase", PERFECT_MONEY_PASS_PRASE)
			.field("Payer_Account", PERFECT_MONEY_WITHDRAWAL_ACCOUNT)
			.field("Payee_Account", perfectMoneyAccoutObject.getWidthdrawalAccount())
			.field("Amount", createWithdrawal.getAmount().toString())
			.field("Memo", subscriptionPaymentMemo)
			.asString();
			perfectMoneyPaymentResponseBodyString = perfectMoneyPaymentResponse.getBody().toString();
			// get the payment status code
			perfectMoneyPaymentResponseStatus =  perfectMoneyPaymentResponse.getStatus();
		
			
		} catch (Exception e) {
			return ResponseEntity
				.badRequest()
				.body(
					new ErrorResponse(
							400,
							"perfect-money/request-failed",
							"perfect money request failed: "+e.toString()
					)
				);

		}
		
		// check the perfect money payment validation
		String NOT_ENOUGHT_MONEY_ERROR = "Not enough money to pay";
		String INVALID_CREDENTIAL_ERROR = "Can not login with passed AccountID and PassPhrase or API is disabled on this account/IP";
		String INVALID_ACCOUNT_TYPE_ERROR = "Payee and payer accounts has different types";
		String INVALID_PAYER_ACCOUNT = "Invalid Payer_Account";
		if(perfectMoneyPaymentResponseBodyString.contains(INVALID_CREDENTIAL_ERROR)) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            400,
                            "perfect-money/invalid-credential",
                            "invalid perfect money account credential"
                    )
                );
		}
		if(perfectMoneyPaymentResponseBodyString.contains(INVALID_ACCOUNT_TYPE_ERROR)) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            400,
                            "perfect-money/invalid-account-type",
                            "invalid perfect money account type"
                    )
                );
		}
		if(perfectMoneyPaymentResponseBodyString.contains(INVALID_PAYER_ACCOUNT)) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            400,
                            "perfect-money/invalid-payer-account",
                            "invalid perfect money payer account"
                    )
                );
		}
		else if(perfectMoneyPaymentResponseBodyString.contains(NOT_ENOUGHT_MONEY_ERROR)) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            400,
                            "perfect-money/not-enough-money",
                            "invalid perfect money account credential"
                    )
                );
		}
		else if(
				perfectMoneyPaymentResponseBodyString.contains("ERROR")
				&& perfectMoneyPaymentResponseBodyString.contains("Error")
				) {
				System.out.println("PERFECT MONEY PAYMENT ERROR");
				System.out.println(perfectMoneyPaymentResponseBodyString);
				return ResponseEntity
					.badRequest()
					.body(
						new ErrorResponse(
								400,
								"perfect-money/error",
								"perfect money error"
						)
					);
		}
    	
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
