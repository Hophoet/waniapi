package com.wani.waniapi.api.repositories;

import com.wani.waniapi.api.models.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {

}
