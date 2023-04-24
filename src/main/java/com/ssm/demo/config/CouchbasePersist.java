package com.ssm.demo.config;

import com.ssm.demo.entity.StateMachineCouchbaseEntity;
import com.ssm.demo.repo.JpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.service.StateMachineSerialisationService;


//@Component
public class CouchbasePersist implements StateMachinePersist<String, String, String> {

    @Autowired
    StateMachineSerialisationService<String, String> service;


    @Autowired
    StringToByteArrayConverter stringToByteArrayConverter;
    @Autowired
    ByteArrayToStringConverter byteArrayToStringConverter;


    @Autowired
    JpaRepo jpaRepo;


    @Override
    public void write(StateMachineContext<String, String> stateMachineContext, String id) throws Exception {
        StateMachineCouchbaseEntity entity = new StateMachineCouchbaseEntity();
        entity.setId(id);
        entity.setState(stateMachineContext.getState());
        entity.setMachineId(id);
        byte[] contextObject = service.serialiseStateMachineContext(stateMachineContext);
        System.out.println(contextObject);
        entity.setStateMachineContext(contextObject);
        jpaRepo.save(entity);

    }

    @Override
    public StateMachineContext<String, String> read(String id) throws Exception {
        byte[] toString = jpaRepo.findById(id).get().getStateMachineContext();
        return service.deserialiseStateMachineContext(toString);

    }
}
