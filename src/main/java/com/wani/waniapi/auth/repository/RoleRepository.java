package com.wani.waniapi.auth.repository;

import com.wani.waniapi.auth.models.ERole;
import com.wani.waniapi.auth.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
