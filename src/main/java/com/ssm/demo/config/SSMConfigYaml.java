package com.ssm.demo.config;

import com.ssm.demo.entity.Transition;
import com.ssm.demo.repo.JpaRepo;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.boot.StateMachineProperties;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.kryo.KryoStateMachineSerialisationService;
import org.springframework.statemachine.listener.CompositeStateMachineListener;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.monitor.StateMachineMonitor;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.StateMachineSerialisationService;
import org.springframework.statemachine.state.State;

import java.util.Set;

@Configuration
@EnableStateMachineFactory
@Slf4j
public class SSMConfigYaml extends StateMachineConfigurerAdapter<String, String> {

    @Autowired
    private SSMYamlProperty yamlConfiguration;

    @Autowired
    private JpaRepo jpaRepo;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config)
            throws Exception {
                config
                .withConfiguration()
                .autoStartup(true)
                        //.listener( listener())
//                        .and()
//                        .withMonitoring()
//                        .monitor(stateMachineMonitor());
                ;

    }
    @Override
    public void configure(StateMachineStateConfigurer<String, String> states)
            throws Exception {
        states
                .withStates()
                .initial(yamlConfiguration.getInitialState())
                .states(Set.copyOf(yamlConfiguration.getStates()))
                .end(yamlConfiguration.getEndState());
    }
    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {

        for (Transition transition : yamlConfiguration.getTransitions()) {
            transitions
                    .withExternal()
                    .source(transition.getSource())
                    .target(transition.getTarget())
                    .event(transition.getEvent())
            ;
        }
    }



    @Bean
    public StateMachineMonitor<String, String> stateMachineMonitor() {
        return new SSMMonitoring(meterRegistry);
    }
    @Bean
    public StateMachineListenerAdapter<String, String> listener() {
        return new StateMachineListenerAdapter<String, String>() {
            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                if (from == null) {
                    System.out.println("Initial state set to " + to.getId());
                } else if (to == null) {
                    System.out.println("Final state reached: " + from.getId());
                } else {
                    System.out.println("State change to " + to.getId() + " from "+from.getId());
                }
            }
        };


    }

    @Bean
    public StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister(
            MongoDbStateMachineRepository jpaStateMachineRepository) {
        return new MongoDbPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

//    @Bean
//    public StateMachinePersister<String, String, String> stateMachineCouchbasePersister(
//            CouchbasePersist jpaStateMachineRepository) {
//        return new DefaultStateMachinePersister<>(jpaStateMachineRepository);
//    }



    @Bean
    public StateMachineSerialisationService<String, String> stateMachineSerialisationService() {
        return new KryoStateMachineSerialisationService<>();
    }

    @Bean
    public StateMachineListener<String, String> loggingStateListener() {
        return new StateMachineListenerAdapter<String,String>(){

            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                log.info("State changed from {} to {}", from != null ? from.getId() : "none", to.getId());
            }


            @Override
            public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
                log.error("State machine error occurred: {}", exception.getMessage());
            }
        };

    }
}
