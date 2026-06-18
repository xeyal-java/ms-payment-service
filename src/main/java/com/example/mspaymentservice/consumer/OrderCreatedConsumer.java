package com.example.mspaymentservice.consumer;


import com.example.mspaymentservice.config.RabbitMqConfig;
import com.example.mspaymentservice.dto.event.OrderEvent;
import com.example.mspaymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedConsumer {

    private final PaymentService paymentService;

    @RabbitListener(queues = RabbitMqConfig.PAYMENT_QUEUE)
    public void consumeOrderCreatedEvent(OrderEvent event) {
        log.info("RabbitMQ: New ORDER_CREATED event captured from exchange -> {}", event);
        paymentService.processPayment(event);
    }
}
