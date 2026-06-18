package com.example.mspaymentservice.service;

import com.example.mspaymentservice.annotation.CacheEvict;
import com.example.mspaymentservice.annotation.RedisCache;
import com.example.mspaymentservice.config.RabbitMqConfig;
import com.example.mspaymentservice.dto.event.OrderEvent;
import com.example.mspaymentservice.dto.response.PaymentResponse;
import com.example.mspaymentservice.dto.event.PaymentSuccessEvent;
import com.example.mspaymentservice.entity.PaymentEntity;
import com.example.mspaymentservice.exception.PaymentNotFoundException;
import com.example.mspaymentservice.mapper.PaymentMapper;
import com.example.mspaymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.mspaymentservice.enums.PaymentStatus.PENDING;
import static com.example.mspaymentservice.enums.PaymentStatus.SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final PaymentMapper paymentMapper;

    @Override
    @RedisCache(key = "payment_by_order_id_", ttl = 600)
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .findFirst()
                .map(paymentMapper::toResponse)    
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for Order ID: " + orderId));
    }

    @Override
    @Transactional
    @CacheEvict(key = "payment_by_order_id_")
    public void processPayment(OrderEvent event) {
        log.info("Processing payment for Order ID: {} with amount: {} AZN", event.getOrderId(), event.getPrice());

        PaymentEntity pendingPayment = createPendingPayment(event);
        executeBankTransaction(pendingPayment);
    }

    private PaymentEntity createPendingPayment(OrderEvent event) {
        PaymentEntity payment = PaymentEntity.builder()
                .orderId(event.getOrderId())
                .amount(event.getPrice())
                .status(PENDING)
                .build();

        PaymentEntity savedPayment = paymentRepository.save(payment);
        log.info("Pending payment record successfully created with ID: {}", savedPayment.getId());
        return savedPayment;
    }

    private void executeBankTransaction(PaymentEntity payment) {
        log.info("Connecting to Bank API to charge amount: {} AZN...", payment.getAmount());
        payment.setStatus(SUCCESS);
        paymentRepository.save(payment);

        log.info("Bank transaction SUCCESSFUL! Payment ID: {} is now APPROVED.", payment.getId());

        PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                .orderId(payment.getOrderId())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PAYMENT_EXCHANGE,
                RabbitMqConfig.PAYMENT_SUCCESS_ROUTING_KEY,
                event
        );

        log.info("Payment success event sent to RabbitMQ for Order ID: {}", payment.getOrderId());
    }
}