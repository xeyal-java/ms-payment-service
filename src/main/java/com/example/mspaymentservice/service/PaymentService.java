package com.example.mspaymentservice.service;

import com.example.mspaymentservice.dto.event.OrderEvent;
import com.example.mspaymentservice.dto.response.PaymentResponse;

public interface PaymentService {
    void processPayment(OrderEvent event);
    PaymentResponse getPaymentByOrderId(Long orderId);
}
