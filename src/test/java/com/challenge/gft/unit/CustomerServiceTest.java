package com.challenge.gft.unit;

import com.challenge.gft.dtos.customer.CustomerCreateDTO;
import com.challenge.gft.dtos.customer.CustomerReadDTO;
import com.challenge.gft.dtos.customer.CustomerUpdateDTO;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import com.challenge.gft.mapper.CustomerMapper;
import com.challenge.gft.repositories.CustomerRepository;
import com.challenge.gft.services.CustomerService;
import com.challenge.gft.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    private CustomerService sut;
    @Mock
    private CustomerRepository customerRepositoryMock;
    @Mock
    private CustomerMapper customerMapperMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;

    @BeforeEach
    void setup() {
        this.sut = new CustomerServiceImpl(customerRepositoryMock, customerMapperMock, passwordEncoderMock);
    }

    @Test
    @DisplayName("Should find customer with success")
    void should_find_customer_by_id() throws NotFoundException {
        Customer customer = new Customer();
        final var id = 1L;

        when(this.customerRepositoryMock.findById(anyLong())).thenReturn(Optional.of(customer));

        this.sut.findById(id);

        verify(this.customerRepositoryMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should find all customers with success")
    void should_find_all_customers_with_success() {
        this.sut.findAll();

        verify(this.customerRepositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create customer with correct values")
    void should_create_customer_with_correct_values() throws UniqueConstraintViolationException {
        CustomerCreateDTO customerCreateDTO = new CustomerCreateDTO(
                "customer",
                "07524914075",
                "Cidade de Pallet",
                "StrongPassword951!#@"
        );

        final var customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        final var hashedPassword = "StrongPassword951!#@";

        when(this.passwordEncoderMock.encode(anyString())).thenReturn(hashedPassword);

        this.sut.create(customerCreateDTO);

        verify(this.customerRepositoryMock).save(customerArgumentCaptor.capture());

        final var customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertEquals(customerCreateDTO.document(), customerArgumentCaptorValue.getDocument());
        assertEquals(customerCreateDTO.name(), customerArgumentCaptorValue.getName());
        assertEquals(customerCreateDTO.address(), customerArgumentCaptorValue.getAddress());
        assertEquals(hashedPassword, customerArgumentCaptorValue.getPassword());
    }

    @Test
    @DisplayName("Should update customer with correct values")
    void should_update_customer_with_correct_values() throws NotFoundException {
        Customer customer = new Customer();
        CustomerUpdateDTO customerUpdateDTO = new CustomerUpdateDTO(
                "name",
                "address",
                "StrongPassword951!#@"
        );

        final var customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        final Long id = 1L;

        when(this.customerRepositoryMock.findById(id)).thenReturn(Optional.of(customer));
        when(this.passwordEncoderMock.encode(customerUpdateDTO.password())).thenReturn(customerUpdateDTO.password());

        doAnswer(invocationOnMock -> {
            CustomerUpdateDTO customerUpdateDTOInvocationOnMock = invocationOnMock.getArgument(0);
            Customer customerInvocationOnMock = invocationOnMock.getArgument(1);

            customerInvocationOnMock.setName(customerUpdateDTOInvocationOnMock.name());
            customerInvocationOnMock.setAddress(customerUpdateDTOInvocationOnMock.address());
            customerInvocationOnMock.setPassword(customerUpdateDTOInvocationOnMock.password());

            return null;
        }).when(customerMapperMock).updateCustomerFromDTO(customerUpdateDTO, customer);

        this.sut.update(id, customerUpdateDTO);

        verify(this.customerRepositoryMock, times(1)).findById(id);
        verify(this.customerRepositoryMock).save(customerArgumentCaptor.capture());

        final var customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertEquals(customerUpdateDTO.name(), customerArgumentCaptorValue.getName());
        assertEquals(customerUpdateDTO.address(), customerArgumentCaptorValue.getAddress());
        assertEquals(customerUpdateDTO.password(), customerArgumentCaptorValue.getPassword());
    }

    @Test
    @DisplayName("Should delete customer")
    void should_delete_customer() throws NotFoundException {
        Customer customer = new Customer();
        final var customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        final Long id = 1L;

        when(this.customerRepositoryMock.findById(id)).thenReturn(Optional.of(customer));

        this.sut.softDelete(id);

        verify(this.customerRepositoryMock, times(1)).findById(id);
        verify(this.customerRepositoryMock).save(customerArgumentCaptor.capture());

        final var customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertEquals(customer.getDeletedAt(), customerArgumentCaptorValue.getDeletedAt());
    }

    @Test
    @DisplayName("should find all paginated filtered customers")
    void should_find_all_paginated_filtered_customers() {
        Specification<Customer> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = PageRequest.of(0, 10);

        List<Customer> fakeUsers = new ArrayList<>();

        fakeUsers.add(new Customer());

        Page<Customer> fakeUserPage = new PageImpl<>(fakeUsers, pageable, fakeUsers.size());

        when(customerRepositoryMock.findAll(spec, pageable)).thenReturn(fakeUserPage);

        Page<CustomerReadDTO> result = this.sut.getPaginatedFiltered(spec, pageable);

        verify(customerRepositoryMock).findAll(spec, pageable);
        List<CustomerReadDTO> expectedList = fakeUsers.stream()
                .map(customer -> customerMapperMock.customerToCustomerReadDTO(customer))
                .collect(Collectors.toList());

        assertEquals(new PageImpl<>(expectedList, pageable, fakeUsers.size()), result);
    }
}
