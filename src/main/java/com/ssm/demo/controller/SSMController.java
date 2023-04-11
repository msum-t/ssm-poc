package com.ssm.demo.controller;


import com.ssm.demo.entity.Customer;
import com.ssm.demo.entity.StateMachineContextEntity;
import com.ssm.demo.service.SSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ssm-yaml")
public class SSMController {

    @Autowired
    private SSMService service;


    @PostMapping("/saveCustomer")
    public Customer newCustomer(@RequestBody Customer customer) throws Exception {
        Customer customer1 = service.saveCustomer(customer);
        System.out.println(customer1.getCustomerId());
        service.createIntialState(customer1.getCustomerId());
        return customer1;
    }

    @PostMapping("/{id}/salesEvent")
    public Customer salesEvent(@PathVariable String id,@RequestBody Customer customer) throws Exception {
        boolean updateState = service.updateStates(id, "SalesReviewEvent");
        if (updateState){
            customer.setSalesStatus("Completed");
            return service.saveCustomer(customer);
        }
        return service.getById(id);
    }

    @PostMapping("/{id}/rmEvent")
    public Customer rmEvent(@PathVariable String id,@RequestBody Customer customer) throws Exception {
        boolean updateState = service.updateStates(id,"RMReviewEvent");
        if (updateState){
            customer.setRmStatus("Completed");
            return service.saveCustomer(customer);
        }
        return service.getById(id);
    }
    @PostMapping("/{id}/docEvent")
    public Customer docEvent(@PathVariable String id,@RequestBody Customer customer) throws Exception {
        boolean updateState = service.updateStates(id, "DocumentReviewEvent");
        if (updateState){
            customer.setDocStatus("Completed");
            return service.saveCustomer(customer);
        }
        return service.getById(id);
    }
    @PostMapping("/{id}/creditEvent")
    public Customer creditEvent(@PathVariable String id,@RequestBody Customer customer) throws Exception {
        boolean updateState = service.updateStates(id, "CreditEvent");
        if (updateState){
            customer.setCreditStatus("Completed");
            return service.saveCustomer(customer);
        }
        return service.getById(id);
    }
    @PostMapping("/{id}/sdcEvent")
    public Customer sdcEvent(@PathVariable String id,@RequestBody Customer customer) throws Exception {
        boolean updateState = service.updateStates(id, "SDCEvent");
        if (updateState){
            customer.setSdcStatus("Completed");
            return service.saveCustomer(customer);
        }
        return service.getById(id);

    }

    @PostMapping("/{id}/welcome")
    public Customer welcome(@PathVariable String id,@RequestBody Customer customer) throws Exception {
        boolean updateState = service.updateStates(id, "WelcomeLetterEvent");
        if (updateState){
            customer.setWelcomeStatus("Completed");
            return service.saveCustomer(customer);
        }
        return service.getById(id);
    }

}
