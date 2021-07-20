package com.wani.waniapi.api.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.PaymentResponse;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.api.services.PaymentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class PaymentController {
	

    @Autowired
    PaymentService paymentService;
    
    @Autowired
    PaymentRepository paymentRepository;
    

    /*
     * get all payment
     */
    @GetMapping("/admin/payments")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentResponse> getPayments(){
        List<PaymentResponse> paymentResponses = new ArrayList<>();
        List<Payment> payments = paymentRepository.findAll();
        for(Payment payment: payments) {
        	paymentResponses.add(
        			paymentService.getRequestResponse(payment.getId())
        	);
        }
        return paymentResponses;
    }
    
}
