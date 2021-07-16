package com.wani.waniapi.api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.PerfectMoneyAccount;

public interface PerfectMoneyAccountRepository extends MongoRepository<PerfectMoneyAccount, String> {
    Optional<PerfectMoneyAccount> findByUserId(String userId);
}
