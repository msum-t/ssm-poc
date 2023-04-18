package com.ssm.demo.service;

import com.ssm.demo.config.SSMYamlProperty;
import com.ssm.demo.entity.Customer;
import com.ssm.demo.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class SSMService {

    @Autowired
    private StateMachinePersist<String, String, String> stateMachinePersist;


    @Autowired
    private StateMachineFactory<String, String> stateMachineFactory;
    @Autowired
    private SSMYamlProperty property;

    @Autowired
    private StateMachinePersister<String, String, String> stateMachinePersister;

    @Autowired
    private CustomerRepo customerRepo;


    public Mono<Customer> saveCustomer(Customer customer) {
        return customerRepo.save(customer);
    }


    public Mono<Customer> createInitialState(Customer customer) {
        Mono<StateMachine<String, String>> stateMachineMono = Mono.fromCallable(() ->

                {
                    UUID uuid = UUID.randomUUID();
                    customer.setCustomerId(uuid.toString());
                    return stateMachineFactory.getStateMachine(uuid);
                }
        ).subscribeOn(Schedulers.boundedElastic());

        return stateMachineMono.flatMap(sm -> {
            try {
                stateMachinePersister.persist(sm, customer.getCustomerId());
            } catch (Exception e) {
                return Mono.error(e);
            }
            sm.stopReactively().subscribe();
            return customerRepo.save(customer);
        });

    }


    public Mono<Boolean> updateStates(String cId, String events) {
        Mono<StateMachine<String, String>> stateMachineMono = Mono.fromCallable(() -> stateMachinePersister.restore(stateMachineFactory.getStateMachine(cId), cId)).subscribeOn(Schedulers.boundedElastic());


        return stateMachineMono.flatMap(stateMachine -> {
            stateMachine.startReactively().subscribe();
            boolean event = stateMachine.sendEvent(events);
            stateMachine.stopReactively().subscribe();
            if (event) {
                try {
                    stateMachinePersister.persist(stateMachine, cId);
                    return Mono.just(true);
                } catch (Exception e) {
                    return Mono.error(e);
                }
            } else {
                return Mono.just(false);
            }
        });

    }

    public Mono<Customer> getById(String customerId) {
        return customerRepo.findById(customerId);
    }


}
