package com.wani.waniapi.api.repositories;


import com.wani.waniapi.api.models.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findByUserId(String userId);


}
