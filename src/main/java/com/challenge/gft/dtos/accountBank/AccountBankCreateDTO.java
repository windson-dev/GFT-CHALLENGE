package com.challenge.gft.dtos.accountBank;

public record AccountBankCreateDTO(
        String agency,
        Double balance,
        Long customerId
) {
}
