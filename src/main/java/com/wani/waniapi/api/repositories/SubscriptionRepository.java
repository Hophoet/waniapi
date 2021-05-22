package com.wani.waniapi.api.repositories;


import com.wani.waniapi.api.models.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

}
