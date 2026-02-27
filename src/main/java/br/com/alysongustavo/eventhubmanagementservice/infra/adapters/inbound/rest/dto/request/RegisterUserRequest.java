package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "{user.name.not.blank}")
    private String name;

    @NotBlank(message = "{user.email.not.blank}")
    @Email(message = "{user.email.valid}")
    private String email;

}
