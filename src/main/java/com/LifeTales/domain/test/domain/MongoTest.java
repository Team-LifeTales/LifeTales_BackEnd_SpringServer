package com.LifeTales.domain.test.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@Document(collection = "user")
public class MongoTest {
    private String test;
    private int testInt;
}
