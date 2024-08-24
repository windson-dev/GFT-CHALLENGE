package com.challenge.gft.unit;

import com.challenge.gft.dtos.payment.PaymentCreateDTO;
import com.challenge.gft.entities.AccountBank;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.entities.Payment;
import com.challenge.gft.enums.AccountBankStatus;
import com.challenge.gft.exceptions.customs.BadRequestException;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.mapper.PaymentMapper;
import com.challenge.gft.repositories.AccountBankRepository;
import com.challenge.gft.repositories.PaymentRepository;
import com.challenge.gft.services.NotificationService;
import com.challenge.gft.services.PaymentService;
import com.challenge.gft.services.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    private PaymentService sut;
    @Mock
    private PaymentRepository paymentRepositoryMock;
    @Mock
    private AccountBankRepository accountBankRepositoryMock;
    @Mock
    private NotificationService notificationServiceMock;
    @Mock
    private PaymentMapper paymentMapperMock;

    @BeforeEach
    void setup() {
        this.sut = new PaymentServiceImpl(paymentRepositoryMock, accountBankRepositoryMock, notificationServiceMock, paymentMapperMock);
    }

    @Test
    @DisplayName("should create payment with correct values")
    void should_create_payment_with_correct_values() throws NotFoundException, IOException, BadRequestException {
        AccountBank sender = AccountBank.builder()
                .id(1L)
                .status(AccountBankStatus.ACTIVE)
                .agency("001")
                .balance(50.10)
                .build();

        AccountBank receiver = AccountBank.builder()
                .id(2L)
                .status(AccountBankStatus.ACTIVE)
                .agency("002")
                .balance(50.10)
                .build();

        PaymentCreateDTO paymentCreateDTO = new PaymentCreateDTO(
                sender.getId(),
                receiver.getId(),
                20.00
        );

        final var paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        final var accountBankArgumentCaptor = ArgumentCaptor.forClass(AccountBank.class);

        when(this.accountBankRepositoryMock.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(this.accountBankRepositoryMock.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        this.sut.create(paymentCreateDTO);

        verify(this.accountBankRepositoryMock, times(2)).save(accountBankArgumentCaptor.capture());

        verify(this.paymentRepositoryMock).save(paymentArgumentCaptor.capture());

        final var paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();

        assertEquals(paymentCreateDTO.amount(), paymentArgumentCaptorValue.getAmount());
        assertEquals(sender, paymentArgumentCaptorValue.getSenderAccount());
        assertEquals(receiver, paymentArgumentCaptorValue.getReceiverAccount());
    }

    @Test
    @DisplayName("Should find payment with success")
    void should_find_payment_by_id() throws NotFoundException {
        Payment payment = new Payment();
        final var id = 1L;

        when(this.paymentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(payment));

        this.sut.findById(id);

        verify(this.paymentRepositoryMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should find all customers with success")
    void should_find_all_customers_with_success() {
        this.sut.findAll();

        verify(this.paymentRepositoryMock, times(1)).findAll();
    }
}
