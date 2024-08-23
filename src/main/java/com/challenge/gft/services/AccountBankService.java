package com.challenge.gft.services;

import com.challenge.gft.dtos.accountBank.AccountBankCreateDTO;
import com.challenge.gft.dtos.accountBank.AccountBankReadDTO;
import com.challenge.gft.dtos.accountBank.AccountBankUpdateDTO;
import com.challenge.gft.entities.AccountBank;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface AccountBankService {
    void create(final AccountBankCreateDTO accountBankCreateDTO) throws NotFoundException, UniqueConstraintViolationException;

    void update(final Long id, final AccountBankUpdateDTO accountBankUpdateDTO) throws NotFoundException;

    void softDelete(final Long id) throws NotFoundException;

    void activeOrDeactivateAccountBank(final Long id) throws NotFoundException;

    AccountBankReadDTO findById(final Long id) throws NotFoundException;

    List<AccountBankReadDTO> findAll();

    Page<AccountBankReadDTO> getPaginatedFiltered(final Specification<AccountBank> spec, final Pageable pageable);
}
