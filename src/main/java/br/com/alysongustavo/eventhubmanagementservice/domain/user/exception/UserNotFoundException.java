package br.com.alysongustavo.eventhubmanagementservice.domain.user.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long id)
    {
        super("USER_NOT_FOUND", id);
    }
}
