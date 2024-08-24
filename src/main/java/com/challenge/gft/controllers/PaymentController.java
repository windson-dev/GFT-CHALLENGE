package com.challenge.gft.controllers;

import com.challenge.gft.dtos.payment.PaymentCreateDTO;
import com.challenge.gft.exceptions.customs.BadRequestException;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.services.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid PaymentCreateDTO paymentCreateDTO) throws NotFoundException, IOException, BadRequestException {
        paymentService.create(paymentCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
