package br.com.alysongustavo.eventhubmanagementservice.application.user.port.out;

import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;

public interface IamPort {
    String createUser(User user, String role);
}
