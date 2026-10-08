package br.com.voluntplus.users.application.service;

import br.com.voluntplus.users.api.CurrentRole;
import br.com.voluntplus.users.api.CurrentUserRoleNotAuthorizedException;
import br.com.voluntplus.users.api.CurrentUserUnavailableException;
import br.com.voluntplus.users.api.PersonType;
import br.com.voluntplus.users.api.PublicGender;
import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.users.application.exception.AuthenticatedIdentityUnavailableException;
import br.com.voluntplus.users.application.exception.UserProfileNotFoundException;
import br.com.voluntplus.users.application.port.in.RequireCurrentUserUseCase;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UsersApiService implements UsersApi {

	private final UserRepository userRepository;
	private final RequireCurrentUserUseCase requireCurrentUser;
	private final Clock clock;

	public UsersApiService(
			UserRepository userRepository,
			RequireCurrentUserUseCase requireCurrentUser,
			Clock clock) {
		this.userRepository = userRepository;
		this.requireCurrentUser = requireCurrentUser;
		this.clock = clock;
	}

	@Override
	public Optional<UserSummary> findById(UUID userId) {
		Objects.requireNonNull(userId, "userId must not be null");
		return userRepository.findById(userId).map(this::toSummary);
	}

	@Override
	public Map<UUID, UserSummary> findAllByIds(Set<UUID> userIds) {
		Objects.requireNonNull(userIds, "userIds must not be null");
		if (userIds.isEmpty()) {
			return Map.of();
		}
		return userRepository.findAllByIds(Set.copyOf(userIds)).stream()
				.map(this::toSummary)
				.collect(Collectors.toUnmodifiableMap(UserSummary::userId, Function.identity()));
	}

	@Override
	public UserSummary getCurrentUser() {
		try {
			RequireCurrentUserUseCase.CurrentUser user = requireCurrentUser.requireCurrentUser();
			return new UserSummary(
					user.userId(),
					toPublicPersonType(user.personType()),
					toPublicRole(user.currentRole()),
					user.email(),
					user.fullName(),
					user.organizationName(),
					toPublicGender(user.gender()),
					calculateAge(user.birthDate()));
		} catch (UserProfileNotFoundException | AuthenticatedIdentityUnavailableException exception) {
			throw new CurrentUserUnavailableException(
					"The authenticated identity does not have an available VoluntPlus profile",
					exception);
		}
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
				toPublicRole(user.getCurrentRole()),
				user.getEmail(),
				user.getFullName(),
				user.getOrganizationName(),
				toPublicGender(user.getGender()),
				calculateAge(user.getBirthDate()));
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

	private PublicGender toPublicGender(br.com.voluntplus.users.domain.model.Gender gender) {
		if (gender == null) {
			return null;
		}
		return PublicGender.valueOf(gender.name());
	}

	private Integer calculateAge(LocalDate birthDate) {
		return birthDate == null ? null : Period.between(birthDate, LocalDate.now(clock)).getYears();
	}
}
