package com.example.transactionsenderservice.service;

import com.example.transactionsenderservice.dto.RejectedTransactionsResponse;
import com.example.transactionsenderservice.entity.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TransactionSenderService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${booking.system.url}")
    private String bookingSystemUrl;

    private final AtomicBoolean sending = new AtomicBoolean(false);

    public Mono<Void> sendTransactions() {
        if (sending.get()) {
            return Mono.empty();
        }
        sending.set(true);

        return Flux.interval(Duration.ofSeconds(2))
                .takeWhile(i -> sending.get())
                .flatMap(i -> sendTransaction())
                .then();
    }

    public Mono<Void> stopSendingTransactions() {
        sending.set(false);
        return Mono.empty();
    }

    private Mono<Void> sendTransaction() {
        Transaction transaction1 = new Transaction();
        transaction1.setFirstName("John");
        transaction1.setLastName("Doe");
        transaction1.setEmail("john@doe.com");
        transaction1.setAmount(500);
        transaction1.setTransactionTypeNumber("TR0001");

        Transaction transaction2 = new Transaction();
        transaction2.setFirstName("testFirstName");
        transaction2.setLastName("testLastName");
        transaction2.setEmail("test@email.com");
        transaction2.setAmount(200);
        transaction2.setTransactionTypeNumber("TR0002");

        Flux<Transaction> transactionFlux = Flux.just(transaction1, transaction2);

        System.out.println("bookingSystemUrl: " + bookingSystemUrl);
        return webClient.post()
                .uri(bookingSystemUrl + "/transactions")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(transactionFlux, Transaction.class)
                .retrieve()
                .bodyToMono(RejectedTransactionsResponse.class)
                .flatMap(this::handleRejectedTransactions);
    }

    private Mono<Void> handleRejectedTransactions(RejectedTransactionsResponse response) {
        try {
            String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(response);
            System.out.println(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.empty();
    }
}
