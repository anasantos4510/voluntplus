package br.com.voluntplus.users.application.port.out;

import br.com.voluntplus.users.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(UUID id);

	Optional<User> findByClerkUserId(String clerkUserId);
}
