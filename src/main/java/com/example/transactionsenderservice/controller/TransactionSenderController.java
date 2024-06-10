package com.example.transactionsenderservice.controller;

import com.example.transactionsenderservice.service.TransactionSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TransactionSenderController {

    @Autowired
    private TransactionSenderService transactionSenderService;

    @GetMapping("/start-sending-transactions")
    public Mono<Void> sendTransactions() {
        System.out.println("Started");
        return transactionSenderService.sendTransactions();
    }

    @GetMapping("/stop-sending-transactions")
    public Mono<Void> stopTransactions() {

        System.out.println("Stopped");
        return transactionSenderService.stopSendingTransactions();
    }
}
