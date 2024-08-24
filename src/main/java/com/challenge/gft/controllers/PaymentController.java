package com.challenge.gft.controllers;

import com.challenge.gft.dtos.payment.PaymentCreateDTO;
import com.challenge.gft.dtos.payment.PaymentReadDTO;
import com.challenge.gft.entities.Payment;
import com.challenge.gft.exceptions.customs.BadRequestException;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.services.PaymentService;
import com.challenge.gft.specification.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<PaymentReadDTO> findById(@PathVariable(value = "id") final Long id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentReadDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PaymentReadDTO>> getPaginatedFiltered(final SpecificationTemplate.PaymentSpec spec,
                                                                     @RequestParam(value = "customerName", required = false) String customerName,
                                                                     @PageableDefault(
                                                                             page = 0,
                                                                             size = 10,
                                                                             sort = "id",
                                                                             direction = Sort.Direction.DESC) final Pageable pageable) {
        Specification<Payment> combinedSpec = Specification.where(spec);

        if (customerName != null) {
            combinedSpec = combinedSpec.and(SpecificationTemplate.filterPaymentByCustomerName(customerName));
        }

        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaginatedFiltered(combinedSpec, pageable));
    }
}
