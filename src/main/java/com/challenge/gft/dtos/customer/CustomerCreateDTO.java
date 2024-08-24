package com.challenge.gft.dtos.customer;

import com.challenge.gft.annotations.ValidDocument;
import com.challenge.gft.annotations.ValidPassword;
import jakarta.validation.constraints.NotNull;

public record CustomerCreateDTO(
        @NotNull(message = "Mandatory field name")
        String name,
        @NotNull(message = "Mandatory field document")
        @ValidDocument
        String document,
        @NotNull(message = "Mandatory field address")
        String address,
        @NotNull(message = "Mandatory field password")
        @ValidPassword
        String password
) {
}
