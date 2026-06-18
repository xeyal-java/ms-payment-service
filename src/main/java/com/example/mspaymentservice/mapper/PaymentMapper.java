package com.example.mspaymentservice.mapper;

import com.example.mspaymentservice.dto.response.PaymentResponse;
import com.example.mspaymentservice.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(PaymentEntity entity);
}
