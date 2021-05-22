package com.wani.waniapi.api.repositories;

import com.wani.waniapi.api.models.PaymentMethod;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PaymentMethodRepository extends MongoRepository<PaymentMethod, String> {

}
