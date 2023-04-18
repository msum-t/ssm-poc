package com.ssm.demo.repo;

import com.ssm.demo.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends ReactiveMongoRepository<Customer,String> {

}
