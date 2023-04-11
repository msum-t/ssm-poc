package com.ssm.demo.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.monitor.AbstractStateMachineMonitor;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Metrics;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
@Slf4j
public class SSMMonitoring extends AbstractStateMachineMonitor<String, String> {
    private final Counter stateCounter;
    private final Counter transitionCounter;
    private final Timer totalTime;


    public SSMMonitoring(MeterRegistry meterRegistry) {
        stateCounter = Counter.builder("statemachine.states.count")
                .description("Number of state transitions")
                .register(meterRegistry);

        transitionCounter = Counter.builder("statemachine.transitions.count")
                .description("Number of state transitions")
                .register(meterRegistry);

        totalTime = Timer.builder("statemachine.transitions.time")
                .description("Number of state transitions time")
                .register(meterRegistry);

    }

    @Override
    public void transition(StateMachine<String, String> stateMachine, Transition<String, String> transition, long duration) {
        transitionCounter.increment();
        totalTime.record(Duration.of(duration, MILLISECONDS.toChronoUnit()));
        totalTime.mean(MILLISECONDS);
        totalTime.max(MILLISECONDS);
        log.info("This state {} has taken {} ms during transition from {}", stateMachine.getState().getId(), duration, transition);

    }

    @Override
    public void action(StateMachine<String, String> stateMachine, Function<StateContext<String, String>, Mono<Void>> action, long duration) {
        stateCounter.increment();
        totalTime.mean(MILLISECONDS);
    }
}
