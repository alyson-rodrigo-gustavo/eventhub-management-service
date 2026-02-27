package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.iam;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.IamPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.infra.exception.KeycloakException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakIamAdapter implements IamPort {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public String createUser(User user, String realmRole) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        try {
            if (emailExists(usersResource, user.getEmail())) {
                throw new KeycloakException("User with email " + user.getEmail() + " already exists.", null);
            }

            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(user.getEmail());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setFirstName(user.getName());
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue("password");
            credential.setTemporary(true);
            userRepresentation.setCredentials(Collections.singletonList(credential));

            try (Response response = usersResource.create(userRepresentation)) {
                if (response.getStatus() != 201) {
                    String body = response.readEntity(String.class);
                    log.error("Keycloak create user failed. status={} body={}", response.getStatus(), body);
                    throw new KeycloakException("Error creating user in Keycloak. " + body, null);
                }

                String userId = CreatedResponseUtil.getCreatedId(response);

                if (realmRole != null && !realmRole.isBlank()) {
                    assignRealmRoleToUser(realmResource, userId, realmRole.trim());
                }

                return userId;
            }

        } catch (WebApplicationException e) {
            String body = safeReadBody(e);
            log.error("Keycloak API error: status={} body={}", e.getResponse().getStatus(), body);
            throw new KeycloakException("Failed to interact with Keycloak API", e);
        } catch (Exception e) {
            throw new KeycloakException("Failed to create user in Keycloak", e);
        }
    }

    private boolean emailExists(UsersResource usersResource, String email) {
        return !usersResource.searchByEmail(email, true).isEmpty();
    }

    private void assignRealmRoleToUser(RealmResource realmResource, String userId, String roleName) {
        // 1) Busca a role no Realm
        RoleRepresentation roleRep;
        try {
            roleRep = realmResource.roles().get(roleName).toRepresentation();
        } catch (Exception e) {
            throw new KeycloakException("Realm role not found in Keycloak: " + roleName, e);
        }

        // 2) Associa no usuário (realm-level role mapping)
        realmResource.users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(roleRep));

        log.info("User {} assigned realm role {}", userId, roleName);
    }

    private String safeReadBody(WebApplicationException e) {
        try {
            return e.getResponse().readEntity(String.class);
        } catch (Exception ignored) {
            return "<unavailable>";
        }
    }
}