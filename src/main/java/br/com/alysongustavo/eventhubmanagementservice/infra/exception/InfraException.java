package br.com.alysongustavo.eventhubmanagementservice.infra.exception;

import lombok.Getter;

@Getter
public class InfraException extends RuntimeException {
    private final String code;

    public InfraException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
