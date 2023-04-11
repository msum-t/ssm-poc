package com.ssm.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("customer")
@Builder
public class Customer {
    @Id
    private String customerId;

    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private String salesStatus;
    private String rmStatus;
    private String docStatus;
    private String creditStatus;
    private String sdcStatus;
    private String welcomeStatus;

}
