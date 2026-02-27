package br.com.alysongustavo.eventhubmanagementservice.domain.event.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long id)
    {
        super("POLICY_TYPE_NOT_FOUND", id);
    }
}
