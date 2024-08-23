package com.challenge.gft.services.impl;

import com.challenge.gft.dtos.accountBank.AccountBankCreateDTO;
import com.challenge.gft.dtos.accountBank.AccountBankReadDTO;
import com.challenge.gft.dtos.accountBank.AccountBankUpdateDTO;
import com.challenge.gft.entities.AccountBank;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.enums.AccountBankStatus;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import com.challenge.gft.mapper.AccountBankMapper;
import com.challenge.gft.repositories.AccountBankRepository;
import com.challenge.gft.repositories.CustomerRepository;
import com.challenge.gft.services.AccountBankService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountBankServiceImpl implements AccountBankService {
    private final AccountBankRepository accountBankRepository;
    private final CustomerRepository customerRepository;
    private final AccountBankMapper accountBankMapper;

    private AccountBank findAccountBankEntityById(final Long id) throws NotFoundException {
        return accountBankRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account Bank " + id + " not found"));
    }

    @Override
    public void create(final AccountBankCreateDTO accountBankCreateDTO) throws NotFoundException, UniqueConstraintViolationException {
        if (accountBankRepository.existsByAgency(accountBankCreateDTO.agency())) {
            throw new UniqueConstraintViolationException("Agency already exists");
        }

        Customer customer = customerRepository.findById(accountBankCreateDTO.customerId()).orElseThrow(() -> new NotFoundException("Customer not found"));

        AccountBank accountBank = AccountBank.builder()
                .customer(customer)
                .balance(accountBankCreateDTO.balance())
                .agency(accountBankCreateDTO.agency())
                .status(AccountBankStatus.ACTIVE)
                .build();

        accountBankRepository.save(accountBank);
    }

    @Override
    public void update(final Long id, final AccountBankUpdateDTO accountBankUpdateDTO) throws NotFoundException {
        AccountBank accountBank = findAccountBankEntityById(id);
        accountBankMapper.updateAccountBankFromDTO(accountBankUpdateDTO, accountBank);
        accountBankRepository.save(accountBank);
    }

    @Override
    public void softDelete(final Long id) throws NotFoundException {
        AccountBank accountBank = findAccountBankEntityById(id);
        accountBank.setStatus(AccountBankStatus.INACTIVE);
        accountBankRepository.save(accountBank);
    }

    @Override
    public void activeOrDeactivateAccountBank(final Long id) throws NotFoundException {
        AccountBank accountBank = findAccountBankEntityById(id);

        if (accountBank.getStatus() == AccountBankStatus.ACTIVE) {
            accountBank.setStatus(AccountBankStatus.INACTIVE);
        } else {
            accountBank.setStatus(AccountBankStatus.ACTIVE);
        }

        accountBankRepository.save(accountBank);
    }

    @Override
    public AccountBankReadDTO findById(final Long id) throws NotFoundException {
        final var accountBank = findAccountBankEntityById(id);
        return accountBankMapper.accountBankToAccountBankReadDTO(accountBank);
    }

    @Override
    public List<AccountBankReadDTO> findAll() {
        return accountBankMapper.accountBankListToAccountBankReadDTOList(accountBankRepository.findAll());
    }

    @Override
    public Page<AccountBankReadDTO> getPaginatedFiltered(final Specification<AccountBank> spec, final Pageable pageable) {
        Page<AccountBank> accountBankPage = accountBankRepository.findAll(spec, pageable);
        return accountBankPage.map(accountBankMapper::accountBankToAccountBankReadDTO);
    }
}
