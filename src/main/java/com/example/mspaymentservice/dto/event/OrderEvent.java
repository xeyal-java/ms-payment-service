package com.example.mspaymentservice.dto.event;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long courierId;
    private BigDecimal price;
}