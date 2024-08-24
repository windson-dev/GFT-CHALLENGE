package com.challenge.gft.services;

import com.challenge.gft.dtos.payment.PaymentCreateDTO;
import com.challenge.gft.dtos.payment.PaymentReadDTO;
import com.challenge.gft.entities.Payment;
import com.challenge.gft.exceptions.customs.BadRequestException;
import com.challenge.gft.exceptions.customs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;

public interface PaymentService {
    void create(final PaymentCreateDTO paymentCreateDTO) throws NotFoundException, IOException, BadRequestException;

    PaymentReadDTO findById(final Long id) throws NotFoundException;

    List<PaymentReadDTO> findAll();

    Page<PaymentReadDTO> getPaginatedFiltered(final Specification<Payment> spec, final Pageable pageable);
}
