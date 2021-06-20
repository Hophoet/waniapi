package com.wani.waniapi.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wani.waniapi.api.models.Withdrawal;

public interface  WithdrawalRepository  extends MongoRepository<Withdrawal, String> {
	  

}
