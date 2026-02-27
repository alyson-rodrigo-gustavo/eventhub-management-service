package br.com.alysongustavo.eventhubmanagementservice.domain.event.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class UserExistException extends BusinessException {

    public UserExistException(String document)
    {
        super("POLICY_ALREADY_EXISTS", document);
    }
}