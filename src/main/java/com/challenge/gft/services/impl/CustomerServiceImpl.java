package com.challenge.gft.services.impl;

import com.challenge.gft.dtos.customer.CustomerCreateDTO;
import com.challenge.gft.dtos.customer.CustomerReadDTO;
import com.challenge.gft.dtos.customer.CustomerUpdateDTO;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.exceptions.customs.DocumentAlreadyExistException;
import com.challenge.gft.exceptions.customs.EntityNotFoundException;
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

    private Customer findCustomerEntityById(final Long id) throws EntityNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer " + id + " not found"));
    }

    @Override
    public void create(final CustomerCreateDTO customerCreateDTO) throws DocumentAlreadyExistException {
        String normalizedDocument = DocumentUtils.normalizeDocument(customerCreateDTO.document());

        if (customerRepository.existsByDocument(normalizedDocument)) {
            throw new DocumentAlreadyExistException("Document already exists");
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
    public void update(final Long id, CustomerUpdateDTO customerUpdateDTO) throws EntityNotFoundException {
        Customer customer = findCustomerEntityById(id);

        customerMapper.customerUpdateFromCustomerUpdateDTO(customerUpdateDTO, customer);

        customerRepository.save(customer);
    }

    @Override
    public void softDelete(final Long id) throws EntityNotFoundException {
        Customer customer = findCustomerEntityById(id);
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Override
    public CustomerReadDTO findById(final Long id) throws EntityNotFoundException {
        Customer customer = findCustomerEntityById(id);
        return customerMapper.customerReadDTOToCustomer(customer);
    }

    @Override
    public List<CustomerReadDTO> findAll() {
        return customerMapper.customerReadDTOToCustomerList(customerRepository.findAll());
    }

    @Override
    public Page<CustomerReadDTO> getPaginatedFiltered(final Specification<Customer> spec, final Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        return customerPage.map(customerMapper::customerReadDTOToCustomer);
    }
}
