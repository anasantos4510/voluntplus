package br.com.voluntplus.users.application.service;

import br.com.voluntplus.users.application.exception.UserAlreadyRegisteredException;
import br.com.voluntplus.users.application.port.in.CompleteIndividualRegistrationUseCase;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentity;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class CompleteIndividualRegistrationService implements CompleteIndividualRegistrationUseCase {

	private final AuthenticatedIdentityProvider identityProvider;
	private final UserRepository userRepository;
	private final Clock clock;

	public CompleteIndividualRegistrationService(
			AuthenticatedIdentityProvider identityProvider,
			UserRepository userRepository,
			Clock clock) {
		this.identityProvider = identityProvider;
		this.userRepository = userRepository;
		this.clock = clock;
	}

	@Override
	@Transactional
	public IndividualRegistrationResult complete(CompleteIndividualRegistrationCommand command) {
		AuthenticatedIdentity identity = identityProvider.currentIdentity();

		if (userRepository.findByClerkUserId(identity.clerkUserId()).isPresent()) {
			throw new UserAlreadyRegisteredException();
		}

		User user = User.createIndividual(
				UUID.randomUUID(),
				identity.clerkUserId(),
				command.fullName(),
				command.birthDate(),
				identity.email(),
				command.gender(),
				command.initialRole(),
				LocalDate.now(clock));

		User savedUser = userRepository.save(user);
		return new IndividualRegistrationResult(
				savedUser.getId(),
				savedUser.getPersonType(),
				savedUser.getCurrentRole(),
				savedUser.getFullName(),
				savedUser.getBirthDate(),
				savedUser.getGender(),
				savedUser.getEmail());
	}
}
