package br.com.voluntplus.users.infrastructure.persistence;

import br.com.voluntplus.users.application.exception.UserAlreadyRegisteredException;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.User;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

	private final SpringDataUserJpaRepository repository;

	public JpaUserRepositoryAdapter(SpringDataUserJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public User save(User user) {
		try {
			UserJpaEntity savedUser = repository.saveAndFlush(UserPersistenceMapper.toEntity(user));
			return UserPersistenceMapper.toDomain(savedUser);
		} catch (DataIntegrityViolationException exception) {
			if (isClerkIdentityConstraintViolation(exception)) {
				throw new UserAlreadyRegisteredException(exception);
			}
			throw exception;
		}
	}

	@Override
	public Optional<User> findById(UUID id) {
		return repository.findById(id).map(UserPersistenceMapper::toDomain);
	}

	@Override
	public List<User> findAllByIds(Set<UUID> ids) {
		return repository.findAllById(ids).stream()
				.map(UserPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public Optional<User> findByClerkUserId(String clerkUserId) {
		return repository.findByClerkUserId(clerkUserId).map(UserPersistenceMapper::toDomain);
	}

	private boolean isClerkIdentityConstraintViolation(Throwable exception) {
		Throwable current = exception;
		while (current != null) {
			if (current instanceof ConstraintViolationException constraintViolation
					&& "uk_users_clerk_user_id".equalsIgnoreCase(constraintViolation.getConstraintName())) {
				return true;
			}

			String message = current.getMessage();
			if (message != null && message.contains("uk_users_clerk_user_id")) {
				return true;
			}
			current = current.getCause();
		}
		return false;
	}
}
