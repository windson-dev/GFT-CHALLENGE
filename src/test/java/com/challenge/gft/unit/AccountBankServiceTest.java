package com.challenge.gft.unit;

import com.challenge.gft.dtos.accountBank.AccountBankCreateDTO;
import com.challenge.gft.dtos.accountBank.AccountBankReadDTO;
import com.challenge.gft.dtos.accountBank.AccountBankUpdateDTO;
import com.challenge.gft.entities.AccountBank;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import com.challenge.gft.mapper.AccountBankMapper;
import com.challenge.gft.repositories.AccountBankRepository;
import com.challenge.gft.repositories.CustomerRepository;
import com.challenge.gft.services.AccountBankService;
import com.challenge.gft.services.impl.AccountBankServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountBankServiceTest {
    private AccountBankService sut;
    @Mock
    private AccountBankRepository accountBankRepositoryMock;
    @Mock
    private CustomerRepository customerRepositoryMock;
    @Mock
    private AccountBankMapper accountBankMapperMock;

    @BeforeEach
    void setup() {
        this.sut = new AccountBankServiceImpl(accountBankRepositoryMock, customerRepositoryMock, accountBankMapperMock);
    }

    @Test
    @DisplayName("Should create account bank with correct values")
    void should_create_account_bank_with_correct_values() throws NotFoundException, UniqueConstraintViolationException {
        Customer customer = Customer.builder()
                .id(1L)
                .name("customer")
                .document("07524914075")
                .address("Cidade de Pallet")
                .password("StrongPassword951!#@")
                .build();

        AccountBankCreateDTO accountBankCreateDTO = new AccountBankCreateDTO(
                "0000-1",
                150.00,
                customer.getId()
        );

        when(this.customerRepositoryMock.findById(customer.getId())).thenReturn(Optional.of(customer));

        final var customerArgumentCaptor = ArgumentCaptor.forClass(AccountBank.class);

        this.sut.create(accountBankCreateDTO);

        verify(this.accountBankRepositoryMock).save(customerArgumentCaptor.capture());

        final var customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertEquals(accountBankCreateDTO.agency(), customerArgumentCaptorValue.getAgency());
        assertEquals(accountBankCreateDTO.balance(), customerArgumentCaptorValue.getBalance());
        assertEquals(accountBankCreateDTO.customerId(), customerArgumentCaptorValue.getCustomer().getId());
    }

    @Test
    @DisplayName("Should find account bank with success")
    void should_find_account_bank_by_id() throws NotFoundException {
        AccountBank accountBank = new AccountBank();
        final var id = 1L;

        when(this.accountBankRepositoryMock.findById(anyLong())).thenReturn(Optional.of(accountBank));

        this.sut.findById(id);

        verify(this.accountBankRepositoryMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should find all account bank with success")
    void should_find_all_account_bank_with_success() {
        this.sut.findAll();

        verify(this.accountBankRepositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete account bank")
    void should_delete_account_bank() throws NotFoundException {
        AccountBank accountBank = new AccountBank();
        final var customerArgumentCaptor = ArgumentCaptor.forClass(AccountBank.class);
        final Long id = 1L;

        when(this.accountBankRepositoryMock.findById(id)).thenReturn(Optional.of(accountBank));

        this.sut.softDelete(id);

        verify(this.accountBankRepositoryMock, times(1)).findById(id);
        verify(this.accountBankRepositoryMock).save(customerArgumentCaptor.capture());

        final var customerArgumentCaptorValue = customerArgumentCaptor.getValue();

        assertEquals(accountBank.getDeletedAt(), customerArgumentCaptorValue.getDeletedAt());
    }

    @Test
    @DisplayName("Should update account bank with correct values")
    void should_update_account_bank_with_correct_values() throws NotFoundException {
        AccountBank accountBank = new AccountBank();
        AccountBankUpdateDTO accountBankUpdateDTO = new AccountBankUpdateDTO(
                150.00
        );

        final var accountBankArgumentCaptor = ArgumentCaptor.forClass(AccountBank.class);
        final Long id = 1L;

        when(this.accountBankRepositoryMock.findById(id)).thenReturn(Optional.of(accountBank));

        doAnswer(invocationOnMock -> {
            AccountBankUpdateDTO accountUpdateDTOInvocationOnMock = invocationOnMock.getArgument(0);
            AccountBank accountInvocationOnMock = invocationOnMock.getArgument(1);
            accountInvocationOnMock.setBalance(accountUpdateDTOInvocationOnMock.balance());
            return null;
        }).when(accountBankMapperMock).updateAccountBankFromDTO(accountBankUpdateDTO, accountBank);

        this.sut.update(id, accountBankUpdateDTO);

        verify(this.accountBankRepositoryMock, times(1)).findById(id);
        verify(this.accountBankRepositoryMock).save(accountBankArgumentCaptor.capture());

        final var accountBankArgumentCaptorValue = accountBankArgumentCaptor.getValue();
        assertEquals(accountBankUpdateDTO.balance(), accountBankArgumentCaptorValue.getBalance());
    }

    @Test
    @DisplayName("should find all paginated filtered account bank")
    void should_find_all_paginated_filtered_account_bank() {
        Specification<AccountBank> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = PageRequest.of(0, 10);

        List<AccountBank> fakeAccounts = new ArrayList<>();

        fakeAccounts.add(new AccountBank());

        Page<AccountBank> fakeUserPage = new PageImpl<>(fakeAccounts, pageable, fakeAccounts.size());

        when(accountBankRepositoryMock.findAll(spec, pageable)).thenReturn(fakeUserPage);

        Page<AccountBankReadDTO> result = this.sut.getPaginatedFiltered(spec, pageable);

        verify(accountBankRepositoryMock).findAll(spec, pageable);
        List<AccountBankReadDTO> expectedList = fakeAccounts.stream()
                .map(accountBank -> accountBankMapperMock.accountBankToAccountBankReadDTO(accountBank))
                .collect(Collectors.toList());

        assertEquals(new PageImpl<>(expectedList, pageable, fakeAccounts.size()), result);
    }
}
