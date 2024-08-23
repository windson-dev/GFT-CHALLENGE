package com.challenge.gft.mapper;

import com.challenge.gft.dtos.accountBank.AccountBankReadDTO;
import com.challenge.gft.dtos.accountBank.AccountBankUpdateDTO;
import com.challenge.gft.entities.AccountBank;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountBankMapper {
    AccountBankReadDTO accountBankToAccountBankReadDTO(AccountBank accountBank);

    List<AccountBankReadDTO> accountBankListToAccountBankReadDTOList(List<AccountBank> accountBankList);

    void updateAccountBankFromDTO(AccountBankUpdateDTO accountBankUpdateDTO, @MappingTarget AccountBank accountBank);
}
