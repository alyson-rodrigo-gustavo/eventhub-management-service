package br.com.alysongustavo.eventhubmanagementservice.domain.user.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class UserExistException extends BusinessException {

    public UserExistException(String name)
    {
        super("USER_ALREADY_EXISTS", name);
    }
}