package br.com.alysongustavo.eventhubmanagementservice.domain.user.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class UserByEmailNotFoundException extends BusinessException {

    public UserByEmailNotFoundException(String email)
    {
        super("USER_BY_EMAIL_NOT_FOUND", email);
    }
}
