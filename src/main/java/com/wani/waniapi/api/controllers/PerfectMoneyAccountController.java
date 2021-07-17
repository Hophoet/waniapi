package com.wani.waniapi.api.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.PerfectMoneyAccount;
import com.wani.waniapi.api.playload.request.perfectmoneyaccount.CreatePerfectMoneyAccountRequest;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.PerfectMoneyAccountRepository;
import com.wani.waniapi.api.services.AccountService;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;
import com.wani.waniapi.auth.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class PerfectMoneyAccountController {
	
    @Autowired
    PerfectMoneyAccountRepository perfectMonayAccountRepository;

    @PostMapping("/perfect-money-account/set")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity createAccount(
	 	       @AuthenticationPrincipal UserDetailsImpl userDetail,
	 	       @Valid @RequestBody CreatePerfectMoneyAccountRequest createPerfectMoneyAccountRequest
	    ){
	    	// create new account object
    		Optional<PerfectMoneyAccount> perfectMoneyAccount = perfectMonayAccountRepository.findByUserId(userDetail.getId());
    		if(perfectMoneyAccount.isPresent()) {
    			// update the perfect money if it present
    			PerfectMoneyAccount perfectMoneyObject =  perfectMoneyAccount.get();
    			perfectMoneyObject.setWidthdrawalAccount(createPerfectMoneyAccountRequest.getWidthdrawalAccount());
    			PerfectMoneyAccount updatedPerfectMoneyAccount = perfectMonayAccountRepository.save(perfectMoneyObject); 
    			return ResponseEntity.ok(updatedPerfectMoneyAccount);
    		}
    		// create new perfect money if the user don't have a perfect money yet
    		PerfectMoneyAccount perfectMoneyObject = new PerfectMoneyAccount(
    				userDetail.getId(),
    				createPerfectMoneyAccountRequest.getWidthdrawalAccount()
    				);
    		PerfectMoneyAccount newPerfectMoneyAccount = perfectMonayAccountRepository.save(perfectMoneyObject); 
    		return ResponseEntity.ok(newPerfectMoneyAccount);
	    }
}
