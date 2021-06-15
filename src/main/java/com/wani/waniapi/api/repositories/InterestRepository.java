package com.wani.waniapi.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wani.waniapi.api.models.Interest;

public interface InterestRepository extends MongoRepository<Interest, String> {

}
