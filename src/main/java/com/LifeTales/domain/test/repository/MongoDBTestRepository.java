package com.LifeTales.domain.test.repository;

import com.LifeTales.domain.test.domain.MongoTest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDBTestRepository extends MongoRepository<MongoTest, String> {
    MongoTest findByTest(String test);
}
