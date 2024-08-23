package com.challenge.gft.dtos.accountBank;

import com.challenge.gft.dtos.customer.CustomerReadDTO;
import com.challenge.gft.enums.AccountBankStatus;

public record AccountBankReadDTO(
        Long id,
        String agency,
        Double balance,
        AccountBankStatus status,
        CustomerReadDTO customer
) {
}
