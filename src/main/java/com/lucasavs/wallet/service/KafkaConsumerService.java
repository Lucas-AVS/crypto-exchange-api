package com.lucasavs.wallet.service;

import com.lucasavs.wallet.event.OrderMatchedEvent;
import com.lucasavs.wallet.event.TransactionFailedEvent;
import com.lucasavs.wallet.event.TransactionSucceededEvent;
import com.lucasavs.wallet.exception.InsufficientFundsException;
import com.lucasavs.wallet.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private static final String TOPIC_ORDER_MATCHED = "order.matched";
    private static final String TOPIC_TRANSACTION_SUCCEEDED = "transaction.succeeded";
    private static final String TOPIC_TRANSACTION_FAILED = "transaction.failed";

    private final AccountService accountService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaConsumerService(AccountService accountService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.accountService = accountService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = TOPIC_ORDER_MATCHED, groupId = "wallet-consumers")
    public void handleOrderMatched(OrderMatchedEvent event) {
        //TODO: Add IDEMPOTENCE logic to cover duplicated order.matched event

        log.info("Received order.matched event for orderId: {}", event.orderId());

        try {
            if (event.side().equals("BUY")) {
                processBuyTransaction(event);
            } else {
                processSellTransaction(event);
            }

            // SUCCEEDED
            log.info("Transaction SUCCEEDED for orderId: {}", event.orderId());
            TransactionSucceededEvent successEvent = new TransactionSucceededEvent(event.orderId(), event.executedPrice());
            kafkaTemplate.send(TOPIC_TRANSACTION_SUCCEEDED, successEvent);

        } catch (InsufficientFundsException | ResourceNotFoundException e) {
            // FAILED
            log.warn("Transaction FAILED for orderId: {}. Reason: {}", event.orderId(), e.getMessage());
            TransactionFailedEvent failedEvent = new TransactionFailedEvent(event.orderId(), e.getMessage());
            kafkaTemplate.send(TOPIC_TRANSACTION_FAILED, failedEvent);
        } catch (Exception e) {
            log.error("CRITICAL: Unexpected error processing orderId: {}", event.orderId(), e);
            TransactionFailedEvent failedEvent = new TransactionFailedEvent(event.orderId(), e.getMessage());
            kafkaTemplate.send(TOPIC_TRANSACTION_FAILED, failedEvent);
            throw e;
        }
    }

    @Transactional
    private void processBuyTransaction(OrderMatchedEvent event) {
        BigDecimal totalCost = event.amountBase().multiply(event.executedPrice());
        // QUOTE
        accountService.updateBalance(
                event.userId(),
                event.quoteAssetSymbol(),
                totalCost.negate()
        );
        // BASE
        accountService.updateBalance(
                event.userId(),
                event.baseAssetSymbol(),
                event.amountBase()
        );
    }

    @Transactional
    private void processSellTransaction(OrderMatchedEvent event) {
        BigDecimal totalRevenue = event.amountBase().multiply(event.executedPrice());
        // BASE
        accountService.updateBalance(
                event.userId(),
                event.baseAssetSymbol(),
                event.amountBase().negate()
        );

        // QUOTE
        accountService.updateBalance(
                event.userId(),
                event.quoteAssetSymbol(),
                totalRevenue
        );
    }
}
