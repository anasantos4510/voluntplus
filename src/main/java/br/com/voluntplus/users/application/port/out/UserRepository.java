package br.com.voluntplus.users.application.port.out;

import br.com.voluntplus.users.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(UUID id);

	List<User> findAllByIds(Set<UUID> ids);

	Optional<User> findByClerkUserId(String clerkUserId);
}
