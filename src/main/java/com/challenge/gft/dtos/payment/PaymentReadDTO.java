package com.challenge.gft.dtos.payment;

import com.challenge.gft.dtos.accountBank.AccountBankReadDTO;

public record PaymentReadDTO(
        Double amount,
        AccountBankReadDTO senderAccount,
        AccountBankReadDTO receiverAccount
) {
}
