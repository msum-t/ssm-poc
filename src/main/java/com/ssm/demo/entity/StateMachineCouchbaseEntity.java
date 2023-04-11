package com.ssm.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.statemachine.data.RepositoryStateMachine;


@Data
@NoArgsConstructor
@AllArgsConstructor
//@Document
public class StateMachineCouchbaseEntity extends RepositoryStateMachine {
    @Id
    private String id;
    private String machineId;
    private String state;

    private byte[] stateMachineContext;



}