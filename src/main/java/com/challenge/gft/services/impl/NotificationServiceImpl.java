package com.challenge.gft.services.impl;

import com.challenge.gft.dtos.notification.NotificationDTO;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.entities.Notification;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.repositories.CustomerRepository;
import com.challenge.gft.repositories.NotificationRepository;
import com.challenge.gft.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final String API_URL_TEMPLATE_RECEIVED = "https://run.mocky.io/v3/8c28daaf-183f-4c40-b808-56edabea602d";
    private static final String API_URL_TEMPLATE_SENT = "https://run.mocky.io/v3/9b0eefed-a724-482e-a14d-1f38e98cbfbc";

    private final NotificationRepository notificationRepository;
    private final CustomerRepository customerRepository;
    private RestTemplate restTemplate;

    @Override
    public void create(final Long senderId, final Long receiverId, final Double amount) throws IOException, NotFoundException {
        Customer customerSender = customerRepository.findById(senderId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Customer customerReceiver = customerRepository.findById(receiverId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        final var responseReceived = restTemplate.exchange(API_URL_TEMPLATE_RECEIVED, HttpMethod.GET, new HttpEntity<>("headers"), NotificationDTO.class);
        final var responseSent = restTemplate.exchange(API_URL_TEMPLATE_SENT, HttpMethod.GET, new HttpEntity<>("headers"), NotificationDTO.class);

        if (responseReceived.getBody() != null && responseSent.getBody() != null) {
            Notification senderNotification = Notification.builder()
                    .text(responseSent.getBody().message() + ", " + customerReceiver.getName() + " $" + amount)
                    .customer(customerSender)
                    .build();
            notificationRepository.save(senderNotification);

            Notification receiverNotification = Notification.builder()
                    .text(responseReceived.getBody().message() + ", " + customerSender.getName() + " $" + amount)
                    .customer(customerReceiver)
                    .build();
            notificationRepository.save(receiverNotification);
        }
    }
}
