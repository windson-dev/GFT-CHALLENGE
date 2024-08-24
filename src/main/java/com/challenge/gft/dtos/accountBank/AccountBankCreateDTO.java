package com.challenge.gft.dtos.accountBank;

import jakarta.validation.constraints.NotNull;

public record AccountBankCreateDTO(
        @NotNull(message = "Mandatory field agency")
        String agency,
        @NotNull(message = "Mandatory field balance")
        Double balance,
        @NotNull(message = "Mandatory field customerId")
        Long customerId
) {
}
