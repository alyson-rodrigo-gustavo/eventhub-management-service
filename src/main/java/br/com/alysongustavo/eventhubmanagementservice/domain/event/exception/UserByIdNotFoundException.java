package br.com.alysongustavo.eventhubmanagementservice.domain.event.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class UserByIdNotFoundException extends BusinessException {

    public UserByIdNotFoundException(Long id)
    {
        super("POLICY_NOT_FOUND", id);
    }
}
