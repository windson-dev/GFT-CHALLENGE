package com.challenge.gft.mapper;

import com.challenge.gft.dtos.customer.CustomerReadDTO;
import com.challenge.gft.dtos.customer.CustomerUpdateDTO;
import com.challenge.gft.entities.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    CustomerReadDTO customerToCustomerReadDTO(Customer customer);

    List<CustomerReadDTO> customerListToCustomerReadDTOList(List<Customer> customer);

    void updateCustomerFromDTO(CustomerUpdateDTO customerUpdateDTO, @MappingTarget Customer customer);
}