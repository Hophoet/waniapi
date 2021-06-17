package com.wani.waniapi.api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wani.waniapi.api.models.Account;


public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByUserId(String userId);

}
