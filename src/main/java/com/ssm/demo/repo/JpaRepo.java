package com.ssm.demo.repo;

import com.ssm.demo.entity.StateMachineCouchbaseEntity;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRepo extends StateMachineRepository<StateMachineCouchbaseEntity> {
}
