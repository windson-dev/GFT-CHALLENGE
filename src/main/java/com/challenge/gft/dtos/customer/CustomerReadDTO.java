package com.challenge.gft.dtos.customer;

public record CustomerReadDTO(
        Long id,
        String name,
        String document,
        String address
) {
}
