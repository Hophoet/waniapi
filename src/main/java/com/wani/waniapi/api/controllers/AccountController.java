package com.wani.waniapi.api.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/v1")
public class AccountController {
	
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

}
