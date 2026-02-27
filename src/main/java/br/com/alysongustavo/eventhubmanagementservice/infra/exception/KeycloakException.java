package br.com.alysongustavo.eventhubmanagementservice.infra.exception;

public class KeycloakException extends InfraException {

    public KeycloakException(String message, Throwable cause) {
        super("KEYCLOAK_UNAVAILABLE", message, cause);
    }
}
