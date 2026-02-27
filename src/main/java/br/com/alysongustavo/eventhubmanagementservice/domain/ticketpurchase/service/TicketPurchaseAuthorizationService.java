package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service;

import br.com.alysongustavo.eventhubmanagementservice.domain.user.exception.UserNotAllowedToPurchaseTicketException;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;

public class TicketPurchaseAuthorizationService {

    public void validateBuyer(User user) {

        if (user.getUserType() != UserType.PARTICIPANT) {
            throw new UserNotAllowedToPurchaseTicketException(user.getName());
        }
    }

    public boolean canPurchase(User user) {
        return user.getUserType() == UserType.PARTICIPANT;
    }
}