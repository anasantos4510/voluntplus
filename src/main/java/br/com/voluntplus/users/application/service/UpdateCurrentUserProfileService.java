package br.com.voluntplus.users.application.service;

import br.com.voluntplus.users.application.exception.UserProfileNotFoundException;
import br.com.voluntplus.users.application.port.in.UpdateCurrentUserProfileUseCase;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.exception.InvalidUserProfileUpdateException;
import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class UpdateCurrentUserProfileService implements UpdateCurrentUserProfileUseCase {

	private final AuthenticatedIdentityProvider identityProvider;
	private final UserRepository userRepository;
	private final Clock clock;

	public UpdateCurrentUserProfileService(
			AuthenticatedIdentityProvider identityProvider,
			UserRepository userRepository,
			Clock clock) {
		this.identityProvider = identityProvider;
		this.userRepository = userRepository;
		this.clock = clock;
	}

	@Override
	@Transactional
	public UpdateCurrentUserProfileResult update(UpdateCurrentUserProfileCommand command) {
		String clerkUserId = identityProvider.currentClerkUserId();
		User user = userRepository.findByClerkUserId(clerkUserId)
				.orElseThrow(UserProfileNotFoundException::new);

		if (user.getPersonType() == PersonType.INDIVIDUAL) {
			if (command.organizationName() != null || command.cnpj() != null) {
				throw new InvalidUserProfileUpdateException(
						"An individual cannot have organization profile data");
			}
			user.updateIndividualProfile(
					command.fullName(),
					command.birthDate(),
					command.gender(),
					LocalDate.now(clock));
		} else {
			if (command.fullName() != null || command.birthDate() != null || command.gender() != null) {
				throw new InvalidUserProfileUpdateException(
						"An organization cannot have individual profile data");
			}
			user.updateOrganizationProfile(command.organizationName(), command.cnpj());
		}

		User savedUser = userRepository.save(user);
		return new UpdateCurrentUserProfileResult(
				savedUser.getId(),
				savedUser.getPersonType(),
				savedUser.getFullName(),
				savedUser.getOrganizationName(),
				savedUser.getCnpj(),
				savedUser.getEmail(),
				savedUser.getBirthDate(),
				savedUser.getGender(),
				savedUser.getCurrentRole());
	}
}
