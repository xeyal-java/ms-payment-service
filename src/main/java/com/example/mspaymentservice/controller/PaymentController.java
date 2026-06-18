package com.example.mspaymentservice.controller;
import com.example.mspaymentservice.entity.PaymentEntity;
import com.example.mspaymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;

    @GetMapping("/order/{orderId}")
    public List<PaymentEntity> getPaymentsByOrderId(@PathVariable Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
