package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity;

import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_seq_generator"
    )
    @SequenceGenerator(
            name = "users_seq_generator",
            sequenceName = "users_seq",
            allocationSize = 1
    )
    private Long id;

    private String name;

    private String email;

    @Column(name = "keycloak_id")
    private String keycloakId;

    private String role;

    @Column(name = "user_type")
    private UserType userType;
}
