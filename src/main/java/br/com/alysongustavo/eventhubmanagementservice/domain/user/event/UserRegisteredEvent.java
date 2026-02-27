package br.com.alysongustavo.eventhubmanagementservice.domain.user.event;

import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {

    private Long userId;
    private String email;
    private String name;
    private String role;
    private UserType type;
}
