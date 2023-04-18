package com.ssm.demo;

import com.ssm.demo.config.SSMYamlProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;
import reactor.core.scheduler.ReactorBlockHoundIntegration;

import java.util.Locale;

@SpringBootApplication
@EnableAutoConfiguration
public class SsmPocApplication implements CommandLineRunner {



	@Autowired
	private SSMYamlProperty yamlConfiguration;


	public static void main(String[] args) {
		//BlockHound.install();

		SpringApplication.run(SsmPocApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(yamlConfiguration.getInitialState());
		System.out.println(yamlConfiguration.getStates());
		System.out.println(yamlConfiguration.getTransitions());



	}
}

