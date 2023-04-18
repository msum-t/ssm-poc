package com.ssm.demo.config;

import com.ssm.demo.repo.ReactiveMangoDbSupport;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineContextRepository;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.service.StateMachineSerialisationService;
import org.springframework.stereotype.Component;


public class ReactiveSupport implements StateMachinePersist<String, String, String> {

    @Autowired
private ReactiveMangoDbSupport reactiveMangoDbSupport;

    @Override
    public void write(StateMachineContext<String, String> stateMachineContext, String s) throws Exception {
        // TODO document why this method is empty
        MongoDbRepositoryStateMachine stateMachine=new MongoDbRepositoryStateMachine();
        stateMachine.setId(s);
       // stateMachine.setStateMachineContext(stateMachineContext);
    }

    @Override
    public StateMachineContext<String, String> read(String s) throws Exception {
        return null;
    }
}
