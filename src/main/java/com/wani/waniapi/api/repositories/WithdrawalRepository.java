package com.wani.waniapi.api.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wani.waniapi.api.models.Withdrawal;

public interface  WithdrawalRepository  extends MongoRepository<Withdrawal, String> {
    List<Withdrawal> findByUserId(String userId);
}
