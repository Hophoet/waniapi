package com.wani.waniapi.api.repositories;


import com.wani.waniapi.api.models.SubscriptionPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriptionPlanRepository extends MongoRepository<SubscriptionPlan, String> {
    Optional<SubscriptionPlan> findByName(String name);

    // Boolean existsByUsername(String username);

    // Boolean existsByEmail(String email);

    // Boolean existsByReference(String reference);
}
