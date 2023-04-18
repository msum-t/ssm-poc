package com.ssm.demo.controller;


import com.ssm.demo.entity.Customer;
import com.ssm.demo.service.SSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ssm-yaml")
public class SSMController {

    @Autowired
    private SSMService service;


    @PostMapping("/saveCustomer")
    public Mono<Customer> newCustomer(@RequestBody Customer customer) throws Exception {
        return service.createInitialState(customer);


    }

    @PostMapping("/{id}/salesEvent")
    public Mono<Customer> salesEvent(@PathVariable String id, @RequestBody Customer customer) throws Exception {
        Mono<Boolean> updateState = service.updateStates(id, "SalesReviewEvent");

        return updateState.flatMap(bool -> {
            if (bool) {
                customer.setSalesStatus("Completed");
                return service.saveCustomer(customer);
            } else
                return service.getById(id);

        });

    }

    @PostMapping("/{id}/rmEvent")
    public Mono<Customer> rmEvent(@PathVariable String id, @RequestBody Customer customer) throws Exception {
        Mono<Boolean> updateState = service.updateStates(id, "RMReviewEvent");
        return updateState.flatMap(bool -> {
            if (bool) {
                customer.setRmStatus("Completed");
                return service.saveCustomer(customer);
            } else
                return service.getById(id);

        });
    }

    @PostMapping("/{id}/docEvent")
    public Mono<Customer> docEvent(@PathVariable String id, @RequestBody Customer customer) throws Exception {
        Mono<Boolean> updateState = service.updateStates(id, "DocumentReviewEvent");
        return updateState.flatMap(bool -> {
            if (bool) {
                customer.setDocStatus("Completed");
                return service.saveCustomer(customer);
            } else
                return service.getById(id);

        });
    }

    @PostMapping("/{id}/creditEvent")
    public Mono<Customer> creditEvent(@PathVariable String id, @RequestBody Customer customer) throws Exception {
        Mono<Boolean> updateState = service.updateStates(id, "CreditEvent");
        return updateState.flatMap(bool -> {
            if (bool) {
                customer.setCreditStatus("Completed");
                return service.saveCustomer(customer);
            } else
                return service.getById(id);

        });
    }

    @PostMapping("/{id}/sdcEvent")
    public Mono<Customer> sdcEvent(@PathVariable String id, @RequestBody Customer customer) throws Exception {
        Mono<Boolean> updateState = service.updateStates(id, "SDCEvent");
        return updateState.flatMap(bool -> {
            if (bool) {
                customer.setSdcStatus("Completed");
                return service.saveCustomer(customer);
            } else
                return service.getById(id);

        });

    }

    @PostMapping("/{id}/welcome")
    public Mono<Customer> welcome(@PathVariable String id, @RequestBody Customer customer) throws Exception {
        Mono<Boolean> updateState = service.updateStates(id, "WelcomeLetterEvent");
        return updateState.flatMap(bool -> {
            if (bool) {
                customer.setWelcomeStatus("Completed");
                return service.saveCustomer(customer);
            } else
                return service.getById(id);

        });
    }


}
