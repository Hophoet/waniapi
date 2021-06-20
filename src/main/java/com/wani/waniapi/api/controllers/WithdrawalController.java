package com.wani.waniapi.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wani.waniapi.api.models.Withdrawal;
import com.wani.waniapi.api.repositories.WithdrawalRepository;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/v1")
public class WithdrawalController {
	
    @Autowired
    WithdrawalRepository withdrawalRepository;

   
	
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

}
