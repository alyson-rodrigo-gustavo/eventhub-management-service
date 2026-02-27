package br.com.alysongustavo.eventhubmanagementservice.domain.user.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class UserNotAllowedToPurchaseTicketException extends BusinessException {

    public UserNotAllowedToPurchaseTicketException(String name)
    {
        super("USER_NOT_ALLOWED_TO_PURCHASE_TICKET", name);
    }
}
