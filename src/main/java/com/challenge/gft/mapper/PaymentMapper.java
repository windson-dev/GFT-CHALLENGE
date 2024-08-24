package com.challenge.gft.mapper;

import com.challenge.gft.dtos.payment.PaymentReadDTO;
import com.challenge.gft.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper {
    PaymentReadDTO paymentToPaymentReadDTO(Payment payment);
    List<PaymentReadDTO> paymentListToPaymentReadDTOList(List<Payment> paymentList);
}
