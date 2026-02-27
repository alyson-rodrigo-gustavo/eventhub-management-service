package br.com.alysongustavo.eventhubmanagementservice.application.user.port.in;


import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;

public interface RegisterUserUseCasePort {
    User registerUser(String name, String email, UserType userType, String role);
}
