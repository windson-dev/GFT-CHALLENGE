package com.challenge.gft.services.impl;

import com.challenge.gft.dtos.customer.CustomerCreateDTO;
import com.challenge.gft.dtos.customer.CustomerReadDTO;
import com.challenge.gft.dtos.customer.CustomerUpdateDTO;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import com.challenge.gft.mapper.CustomerMapper;
import com.challenge.gft.repositories.CustomerRepository;
import com.challenge.gft.services.CustomerService;
import com.challenge.gft.utils.DocumentUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    private Customer findCustomerEntityById(final Long id) throws NotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer " + id + " not found"));
    }

    @Override
    public void create(final CustomerCreateDTO customerCreateDTO) throws UniqueConstraintViolationException {
        String normalizedDocument = DocumentUtils.normalizeDocument(customerCreateDTO.document());

        if (customerRepository.existsByDocument(normalizedDocument)) {
            throw new UniqueConstraintViolationException("Document already exists");
        }

        Customer customer = Customer.builder()
                .name(customerCreateDTO.name())
                .document(normalizedDocument)
                .address(customerCreateDTO.address())
                .password(passwordEncoder.encode(customerCreateDTO.password()))
                .build();

        customerRepository.save(customer);
    }

    @Override
    public void update(final Long id, CustomerUpdateDTO customerUpdateDTO) throws NotFoundException {
        Customer customer = findCustomerEntityById(id);
        customerMapper.updateCustomerFromDTO(customerUpdateDTO, customer);

        if (customerUpdateDTO.password() != null) {
            customer.setPassword(passwordEncoder.encode(customerUpdateDTO.password()));
        }

        customerRepository.save(customer);
    }

    @Override
    public void softDelete(final Long id) throws NotFoundException {
        Customer customer = findCustomerEntityById(id);
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Override
    public CustomerReadDTO findById(final Long id) throws NotFoundException {
        Customer customer = findCustomerEntityById(id);
        return customerMapper.customerToCustomerReadDTO(customer);
    }

    @Override
    public List<CustomerReadDTO> findAll() {
        return customerMapper.customerListToCustomerReadDTOList(customerRepository.findAll());
    }

    @Override
    public Page<CustomerReadDTO> getPaginatedFiltered(final Specification<Customer> spec, final Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        return customerPage.map(customerMapper::customerToCustomerReadDTO);
    }
}
