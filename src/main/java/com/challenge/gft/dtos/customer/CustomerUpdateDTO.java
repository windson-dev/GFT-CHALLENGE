package com.challenge.gft.dtos.customer;

public record CustomerUpdateDTO(
        String name,
        String address,
        String password
) {
}
