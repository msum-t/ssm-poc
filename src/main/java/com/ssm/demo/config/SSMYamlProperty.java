package com.ssm.demo.config;

import com.ssm.demo.entity.Transition;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "state-machine")
@Data
public class SSMYamlProperty {

    private String initialState;

    private String endState;

    private List<String> states;

    private HashSet<String> events;

    private List<Transition> transitions;



}
