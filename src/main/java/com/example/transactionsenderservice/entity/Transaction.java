package com.example.transactionsenderservice.entity;

import lombok.Data;

@Data
public class Transaction {

    private Long transactionUniqueId;

    private String firstName;

    private String lastName;

    private String email;

    private double amount;

    private String transactionTypeNumber;
}
