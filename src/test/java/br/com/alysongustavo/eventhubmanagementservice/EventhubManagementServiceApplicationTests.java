package br.com.alysongustavo.eventhubmanagementservice;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.IamPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class EventhubManagementServiceApplicationTests {

    @MockitoBean
    private IamPort iamPort;

    @MockitoBean
    private JwtDecoder jwtDecoder;

}
