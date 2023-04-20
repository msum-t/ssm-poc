package com.ssm.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class SsmPocApplication {


	public static void main(String[] args) {
		//BlockHound.install();
		SpringApplication.run(SsmPocApplication.class, args);
	}

}

