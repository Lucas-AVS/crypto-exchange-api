package com.lucasavs.wallet.service;

import com.lucasavs.wallet.event.OrderPlacedEvent;
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

    // Topics
    private static final String TOPIC_ORDER_PLACED = "order.placed";
    private static final String TOPIC_TRANSACTION_SUCCEEDED = "transaction.succeeded";
    private static final String TOPIC_TRANSACTION_FAILED = "transaction.failed";

    private final AccountService accountService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaConsumerService(AccountService accountService, KafkaTemplate<String, String> kafkaTemplate) {
        this.accountService = accountService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = TOPIC_ORDER_PLACED, groupId = "wallet-consumers")
    @Transactional
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Received order.placed event for orderId: {}", event.getOrderId());

        try {
            if (event.getSide().equals("BUY")) {
                processBuyTransaction(event);
            } else {
                processSellTransaction(event);
            }

            // SUCCEEDED
            log.info("Transaction SUCCEEDED for orderId: {}", event.getOrderId());
            kafkaTemplate.send(TOPIC_TRANSACTION_SUCCEEDED, event.getOrderId().toString());

        } catch (InsufficientFundsException | ResourceNotFoundException e) {
            // FAILED
            log.warn("Transaction FAILED for orderId: {}. Reason: {}", event.getOrderId(), e.getMessage());
            kafkaTemplate.send(TOPIC_TRANSACTION_FAILED, event.getOrderId().toString());
        } catch (Exception e) {
            log.error("CRITICAL: Unexpected error processing orderId: {}", event.getOrderId(), e);
            kafkaTemplate.send(TOPIC_TRANSACTION_FAILED, event.getOrderId().toString());
        }
    }

    private void processBuyTransaction(OrderPlacedEvent event) {
        BigDecimal totalCost = event.getAmountBase().multiply(event.getTargetPrice());
        // QUOTE
        accountService.updateBalance(
                event.getUserId(),
                event.getQuoteAssetSymbol(),
                totalCost.negate()
        );
        // BASE
        accountService.updateBalance(
                event.getUserId(),
                event.getBaseAssetSymbol(),
                event.getAmountBase()
        );
    }

    private void processSellTransaction(OrderPlacedEvent event) {
        BigDecimal totalRevenue = event.getAmountBase().multiply(event.getTargetPrice());
        // BASE
        accountService.updateBalance(
                event.getUserId(),
                event.getBaseAssetSymbol(),
                event.getAmountBase().negate()
        );

        // QUOTE
        accountService.updateBalance(
                event.getUserId(),
                event.getQuoteAssetSymbol(),
                totalRevenue
        );
    }
}
