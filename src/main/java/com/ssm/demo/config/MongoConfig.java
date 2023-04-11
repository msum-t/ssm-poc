package com.ssm.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public class MongoConfig  {
    @Autowired
    private StateMachineFactory<String, String > stateMachineFactory;
    @Autowired
    private StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister;


    @Bean
    public StateMachineService<String, String> stateMachineService() {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
    }


    @Autowired
    private StateMachinePersist<String, String, String> stateMachinePersist;

    @Bean
    public StateMachinePersister<String, String, String> stateMachinePersister() {
        return new DefaultStateMachinePersister<>(stateMachinePersist);
    }


}
