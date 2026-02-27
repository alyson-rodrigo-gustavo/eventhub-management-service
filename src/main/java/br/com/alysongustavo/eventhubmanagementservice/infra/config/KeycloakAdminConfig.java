package br.com.alysongustavo.eventhubmanagementservice.infra.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Bean
    public Keycloak keycloak() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JacksonJsonProvider jacksonProvider = new JacksonJsonProvider(mapper);

        var resteasyClient = ((ResteasyClientBuilder) ResteasyClientBuilder.newBuilder())
                .register(jacksonProvider)
                .build();

        return KeycloakBuilder.builder()
                .serverUrl(this.serverUrl) // sua URL
                .realm(this.realm)           // seu Realm
                .grantType("client_credentials")
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .resteasyClient(resteasyClient)
                .build();
    }
}
