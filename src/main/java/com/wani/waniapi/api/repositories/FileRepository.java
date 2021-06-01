
package com.wani.waniapi.api.repositories;

import com.wani.waniapi.api.models.File;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {
    // List<Subscription> findByUserId(String userId);


}
