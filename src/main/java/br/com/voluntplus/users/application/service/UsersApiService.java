package br.com.voluntplus.users.application.service;

import br.com.voluntplus.users.api.CurrentRole;
import br.com.voluntplus.users.api.CurrentUserRoleNotAuthorizedException;
import br.com.voluntplus.users.api.PersonType;
import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.users.application.port.in.RequireCurrentUserUseCase;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UsersApiService implements UsersApi {

	private final UserRepository userRepository;
	private final RequireCurrentUserUseCase requireCurrentUser;

	public UsersApiService(
			UserRepository userRepository,
			RequireCurrentUserUseCase requireCurrentUser) {
		this.userRepository = userRepository;
		this.requireCurrentUser = requireCurrentUser;
	}

	@Override
	public Optional<UserSummary> findById(UUID userId) {
		Objects.requireNonNull(userId, "userId must not be null");
		return userRepository.findById(userId).map(this::toSummary);
	}

	@Override
	public UserSummary getCurrentUser() {
		RequireCurrentUserUseCase.CurrentUser user = requireCurrentUser.requireCurrentUser();
		return new UserSummary(
				user.userId(),
				toPublicPersonType(user.personType()),
				toPublicRole(user.currentRole()));
	}

	@Override
	public UserSummary requireCurrentUserWithRole(CurrentRole requiredRole) {
		Objects.requireNonNull(requiredRole, "requiredRole must not be null");
		UserSummary currentUser = getCurrentUser();
		if (currentUser.currentRole() != requiredRole) {
			throw new CurrentUserRoleNotAuthorizedException(currentUser.currentRole(), requiredRole);
		}
		return currentUser;
	}

	private UserSummary toSummary(User user) {
		return new UserSummary(
				user.getId(),
				toPublicPersonType(user.getPersonType()),
				toPublicRole(user.getCurrentRole()));
	}

	private PersonType toPublicPersonType(br.com.voluntplus.users.domain.model.PersonType personType) {
		return switch (personType) {
			case INDIVIDUAL -> PersonType.INDIVIDUAL;
			case ORGANIZATION -> PersonType.ORGANIZATION;
		};
	}

	private CurrentRole toPublicRole(br.com.voluntplus.users.domain.model.UserRole role) {
		return switch (role) {
			case BENEFICIARY -> CurrentRole.BENEFICIARY;
			case OFFERER -> CurrentRole.OFFERER;
		};
	}
}
