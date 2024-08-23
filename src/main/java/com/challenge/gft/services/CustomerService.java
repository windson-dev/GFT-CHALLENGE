package com.challenge.gft.services;

import com.challenge.gft.dtos.customer.CustomerCreateDTO;
import com.challenge.gft.dtos.customer.CustomerReadDTO;
import com.challenge.gft.dtos.customer.CustomerUpdateDTO;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import com.challenge.gft.exceptions.customs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CustomerService {
    void create(final CustomerCreateDTO customerCreateDTO) throws UniqueConstraintViolationException;

    void update(final Long id, final CustomerUpdateDTO customerUpdateDTO) throws NotFoundException;

    void softDelete(final Long id) throws NotFoundException;

    CustomerReadDTO findById(final Long id) throws NotFoundException;

    List<CustomerReadDTO> findAll();

    Page<CustomerReadDTO> getPaginatedFiltered(final Specification<Customer> spec, final Pageable pageable);
}
