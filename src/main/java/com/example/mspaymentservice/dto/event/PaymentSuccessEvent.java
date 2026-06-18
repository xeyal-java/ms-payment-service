package com.example.mspaymentservice.dto.event;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PaymentSuccessEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;
}
