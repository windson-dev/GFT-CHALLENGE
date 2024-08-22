package com.challenge.gft.services;

import com.challenge.gft.dtos.customer.CustomerCreateDTO;
import com.challenge.gft.dtos.customer.CustomerReadDTO;
import com.challenge.gft.dtos.customer.CustomerUpdateDTO;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.exceptions.customs.DocumentAlreadyExistException;
import com.challenge.gft.exceptions.customs.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CustomerService {
    void create(final CustomerCreateDTO customerCreateDTO) throws DocumentAlreadyExistException;
    void update(final Long id, final CustomerUpdateDTO customerUpdateDTO) throws EntityNotFoundException;
    CustomerReadDTO findById(final Long id) throws EntityNotFoundException;
    List<CustomerReadDTO> findAll();
    Page<CustomerReadDTO> getPaginatedFiltered(final Specification<Customer> spec, final Pageable pageable);
    void softDelete(final Long id) throws EntityNotFoundException;
}
