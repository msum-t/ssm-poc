package com.ssm.demo.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.demo.config.SSMYamlProperty;
import com.ssm.demo.entity.Customer;
import com.ssm.demo.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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

    @Autowired
    private ObjectMapper objectMapper;


    public Mono<Customer> saveCustomer(Customer customer) {
        return customerRepo.save(customer);
    }


    public Mono<Customer> createInitialState(Customer customer) {


        //business logic  1k iteration
        Flux.range(1, 1000).subscribe();


        Mono<String> stringUuid = Mono.fromCallable(() -> UUID.randomUUID().toString())
                .subscribeOn(Schedulers.boundedElastic());

        Mono<StateMachine<String, String>> stateMachineMono =

                stringUuid.flatMap(uuid ->
                        Mono.fromCallable(() ->
                                {
                                    customer.setCustomerId(uuid);
                                    return stateMachineFactory.getStateMachine(uuid);
                                }
                        ).subscribeOn(Schedulers.boundedElastic()));

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

        Flux.range(1, 1000).subscribe();

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
