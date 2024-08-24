package com.challenge.gft.services;

import com.challenge.gft.exceptions.customs.NotFoundException;

import java.io.IOException;

public interface NotificationService {
    void create(final Long senderId, final Long receiverId, final Double amount) throws IOException, NotFoundException;
}
