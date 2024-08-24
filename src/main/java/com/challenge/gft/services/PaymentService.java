package com.challenge.gft.services;

import com.challenge.gft.dtos.payment.PaymentCreateDTO;
import com.challenge.gft.exceptions.customs.BadRequestException;
import com.challenge.gft.exceptions.customs.NotFoundException;

import java.io.IOException;

public interface PaymentService {
    void create(final PaymentCreateDTO paymentCreateDTO) throws NotFoundException, IOException, BadRequestException;
}
