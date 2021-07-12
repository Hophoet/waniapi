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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.services.AccountService;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;
import com.wani.waniapi.auth.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AccountController {
	
	 @Autowired
	 private AccountService accountService;
	    
	
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/account/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity createAccount(
	 	       @AuthenticationPrincipal UserDetailsImpl userDetail
	    ){
	    	// create new account object
			Account account = new Account(userDetail.getId());
			// save the account to the database
			Account createdAccount = accountRepository.save(account);
	    	 // response
	        return ResponseEntity.ok(
	        		createdAccount
	        );
	    }
    
    @GetMapping("/account/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAccount(
        @PathVariable String id
    ){
        // get the subscription plan
        Optional<Account> account =  accountRepository.findById(id);
        // check if the account exists
        if(!account.isPresent()){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            404,
                            "account/not-found",
                            "account not found"
                    )
                );
        }
    
        return ResponseEntity.ok(accountService.getRequestResponse(id));
    }

}
