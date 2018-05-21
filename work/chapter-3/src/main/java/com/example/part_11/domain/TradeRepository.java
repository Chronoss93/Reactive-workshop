package com.example.part_11.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TradeRepository extends ReactiveMongoRepository<Trade,ObjectId> {
}
