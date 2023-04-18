package com.ssm.demo.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveMangoDbSupport extends ReactiveMongoRepository<MongoDbRepositoryStateMachine,String> {
}
