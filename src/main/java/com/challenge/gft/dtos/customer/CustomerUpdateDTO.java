package com.challenge.gft.dtos.customer;

import com.challenge.gft.annotations.ValidPassword;

public record CustomerUpdateDTO(
        String name,
        String address,
        @ValidPassword
        String password
) {
}
