package com.ssm.demo.service;

import com.ssm.demo.config.SSMYamlProperty;
import com.ssm.demo.entity.Customer;
import com.ssm.demo.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class SSMService {




    @Autowired
    private StateMachineFactory<String, String> stateMachineFactory;
    @Autowired
    private SSMYamlProperty property;

    @Autowired
    private StateMachinePersister<String, String, String> stateMachinePersister;

    @Autowired
    private CustomerRepo customerRepo;

    public Customer saveCustomer(Customer customer){
        for (int i = 0; i < 1000; i++) {

        }

        return   customerRepo.save(customer);
    }


    public String createIntialState(String customerID) throws Exception {
        StateMachine<String, String> stateMachine = stateMachineFactory.getStateMachine(customerID);
        stateMachinePersister.persist(stateMachine,customerID);
        stateMachine.stopReactively();
        return customerID;

    }


    public boolean updateStates(String cId, String events) throws Exception {
        for (int i = 0; i < 1000; i++) {

        }
        StateMachine<String, String> stateMachine = stateMachinePersister.restore(stateMachineFactory.getStateMachine(cId), cId);
        boolean event = stateMachine.sendEvent(events);
        stateMachinePersister.persist(stateMachine,cId);
        stateMachine.stopReactively();
       return event;

    }

    public Customer getById(String customerId){
        return customerRepo.findById(customerId).get();
    }

}
