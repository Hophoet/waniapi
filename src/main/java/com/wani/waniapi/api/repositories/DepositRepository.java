package com.wani.waniapi.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wani.waniapi.api.models.Deposit;

public interface DepositRepository extends MongoRepository<Deposit, String> {

}
