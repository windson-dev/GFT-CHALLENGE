package com.challenge.gft.services.impl;

import com.challenge.gft.dtos.payment.PaymentCreateDTO;
import com.challenge.gft.dtos.payment.PaymentReadDTO;
import com.challenge.gft.entities.AccountBank;
import com.challenge.gft.entities.Payment;
import com.challenge.gft.enums.AccountBankStatus;
import com.challenge.gft.exceptions.customs.BadRequestException;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.mapper.PaymentMapper;
import com.challenge.gft.repositories.AccountBankRepository;
import com.challenge.gft.repositories.PaymentRepository;
import com.challenge.gft.services.NotificationService;
import com.challenge.gft.services.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountBankRepository accountBankRepository;
    private final NotificationService notificationService;
    private final PaymentMapper paymentMapper;

    private Payment findPaymentByIdEntity(final Long id) throws NotFoundException {
        return paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment not found"));
    }

    @Override
    @Transactional
    public void create(final PaymentCreateDTO paymentCreateDTO) throws NotFoundException, IOException, BadRequestException {
        AccountBank sender = accountBankRepository.findById(paymentCreateDTO.senderAccountId())
                .orElseThrow(() -> new NotFoundException("Sender account not found"));
        AccountBank receiver = accountBankRepository.findById(paymentCreateDTO.receiverAccountId())
                .orElseThrow(() -> new NotFoundException("Receiver account not found"));

        if (sender.getStatus() == AccountBankStatus.INACTIVE || receiver.getStatus() == AccountBankStatus.INACTIVE) {
            throw new BadRequestException("Sender account or receiver account is inactive");
        }

        if (sender.getBalance() < paymentCreateDTO.amount()) {
            throw new BadRequestException("The sender's account has insufficient funds, current balance: $" + sender.getBalance());
        }

        if (paymentCreateDTO.senderAccountId().equals(paymentCreateDTO.receiverAccountId())) {
            throw new BadRequestException("sending account is the same as receiving account");
        }

        sender.setBalance(sender.getBalance() - paymentCreateDTO.amount());
        receiver.setBalance(receiver.getBalance() + paymentCreateDTO.amount());

        accountBankRepository.save(sender);
        accountBankRepository.save(receiver);

        Payment payment = Payment.builder()
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(paymentCreateDTO.amount())
                .build();

        paymentRepository.save(payment);

        try {
            notificationService.create(paymentCreateDTO.senderAccountId(), paymentCreateDTO.receiverAccountId(), paymentCreateDTO.amount());
        } catch (RestClientException e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    @Override
    public PaymentReadDTO findById(final Long id) throws NotFoundException {
        Payment payment = findPaymentByIdEntity(id);
        return paymentMapper.paymentToPaymentReadDTO(payment);
    }

    @Override
    public List<PaymentReadDTO> findAll() {
        return paymentMapper.paymentListToPaymentReadDTOList(paymentRepository.findAll());
    }

    @Override
    public Page<PaymentReadDTO> getPaginatedFiltered(final Specification<Payment> spec, final Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findAll(spec, pageable);
        return paymentPage.map(paymentMapper::paymentToPaymentReadDTO);
    }

}
