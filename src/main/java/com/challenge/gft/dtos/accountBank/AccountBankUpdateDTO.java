package com.challenge.gft.dtos.accountBank;

import jakarta.validation.constraints.NotNull;

public record AccountBankUpdateDTO(
        @NotNull(message = "Mandatory field balance")
        Double balance
) {
}
