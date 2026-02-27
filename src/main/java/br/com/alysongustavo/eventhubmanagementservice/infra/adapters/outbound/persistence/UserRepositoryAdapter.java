
package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.UserRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.mapper.UserMapper;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        var userEntity = userMapper.toEntity(user);
        var savedUserEntity = userRepository.save(userEntity);
        return userMapper.toDomain(savedUserEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDomain);
    }
}
