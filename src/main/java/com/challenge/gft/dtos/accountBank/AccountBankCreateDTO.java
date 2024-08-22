package com.challenge.gft.dtos.accountBank;

import com.challenge.gft.entities.Customer;

public record AccountBankCreateDTO(
        String agency,
        Double balance,
        Customer customer
) {
}
