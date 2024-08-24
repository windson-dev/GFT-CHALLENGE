package com.challenge.gft.dtos.payment;

import jakarta.validation.constraints.NotNull;

public record PaymentCreateDTO(
        @NotNull(message = "Mandatory field senderAccountId")
        Long senderAccountId,
        @NotNull(message = "Mandatory field receiverAccountId")
        Long receiverAccountId,
        @NotNull(message = "Mandatory field amount")
        Double amount
) {
}
