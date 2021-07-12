package com.wani.waniapi.api.services;

import java.time.*;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.AccountResponse;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.repository.UserRepository;

@Service
public class AccountService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	public AccountResponse getRequestResponse( String accountId) {
		AccountResponse accountResponse = new AccountResponse();
	
		Optional<Account> account =  accountRepository.findById(accountId);
        if(!account.isPresent()){
        	// get the user
        	return null;
        }
    	Account accountObject = account.get();
    	accountResponse.setId(accountObject.getId());
		accountResponse.setAmount(accountObject.getAmount());
		accountResponse.setCreatedAt(accountObject.getCreatedAt());
    	// get the user 
    	Optional<User> user = userRepository.findById(accountObject.getUserId());
    	if(user.isPresent()) {
    		// set the user response to the account response
    		accountResponse.setUser(
    				user.get().getRequestResponse()
    		);
    	}

        return accountResponse;
	}
}