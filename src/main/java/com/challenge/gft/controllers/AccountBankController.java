package com.challenge.gft.controllers;

import com.challenge.gft.dtos.accountBank.AccountBankCreateDTO;
import com.challenge.gft.dtos.accountBank.AccountBankReadDTO;
import com.challenge.gft.dtos.accountBank.AccountBankUpdateDTO;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import com.challenge.gft.services.AccountBankService;
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
@RequestMapping("/account-bank")
public class AccountBankController {
    private final AccountBankService accountBankService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid final AccountBankCreateDTO accountBankCreateDTO) throws NotFoundException, UniqueConstraintViolationException {
        accountBankService.create(accountBankCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") final Long id,
                                         @RequestBody final AccountBankUpdateDTO accountBankUpdateDTO) throws NotFoundException {
        accountBankService.update(id, accountBankUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> softDelete(@PathVariable(value = "id") final Long id) throws NotFoundException {
        accountBankService.softDelete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Object> activeOrDeactivateAccountBank(@PathVariable(value = "id") final Long id) throws NotFoundException {
        accountBankService.activeOrDeactivateAccountBank(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountBankReadDTO> findById(@PathVariable(value = "id") final Long id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(accountBankService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AccountBankReadDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(accountBankService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<AccountBankReadDTO>> getPaginatedFiltered(final SpecificationTemplate.AccountBankSpec spec,
                                                                         @PageableDefault(
                                                                                 page = 0,
                                                                                 size = 10,
                                                                                 sort = "id",
                                                                                 direction = Sort.Direction.DESC) final Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(accountBankService.getPaginatedFiltered(spec, pageable));
    }
}
