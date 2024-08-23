package com.challenge.gft.controllers;

import com.challenge.gft.dtos.customer.CustomerCreateDTO;
import com.challenge.gft.dtos.customer.CustomerReadDTO;
import com.challenge.gft.dtos.customer.CustomerUpdateDTO;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import com.challenge.gft.services.CustomerService;
import com.challenge.gft.specification.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid final CustomerCreateDTO customerCreateDTO) throws UniqueConstraintViolationException {
        customerService.create(customerCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") final Long id, @RequestBody CustomerUpdateDTO customerUpdateDTO) throws NotFoundException {
        customerService.update(id, customerUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> softDelete(
            @PathVariable(value = "id") final Long id) throws NotFoundException {
        customerService.softDelete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerReadDTO> findById(@PathVariable(value = "id") final Long id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerReadDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<CustomerReadDTO>> getPaginatedFiltered(final SpecificationTemplate.CustomerSpec spec,
                                                                      @PageableDefault(
                                                                              page = 0,
                                                                              size = 10,
                                                                              sort = "id",
                                                                              direction = Sort.Direction.DESC) final Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getPaginatedFiltered(spec, pageable));
    }
}
