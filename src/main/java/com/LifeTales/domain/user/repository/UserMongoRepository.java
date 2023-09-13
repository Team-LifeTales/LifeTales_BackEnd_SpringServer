package com.LifeTales.domain.user.repository;


import com.LifeTales.domain.user.repository.Mongo.UserBasicData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<UserBasicData, String> {
    boolean existsById(String id);

    Optional<UserBasicData> findById(String id);
}
