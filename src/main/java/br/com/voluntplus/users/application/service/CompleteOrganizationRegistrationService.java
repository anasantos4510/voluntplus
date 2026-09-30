package br.com.voluntplus.users.application.service;

import br.com.voluntplus.users.application.exception.UserAlreadyRegisteredException;
import br.com.voluntplus.users.application.port.in.CompleteOrganizationRegistrationUseCase;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentity;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CompleteOrganizationRegistrationService implements CompleteOrganizationRegistrationUseCase {

	private final AuthenticatedIdentityProvider identityProvider;
	private final UserRepository userRepository;

	public CompleteOrganizationRegistrationService(
			AuthenticatedIdentityProvider identityProvider,
			UserRepository userRepository) {
		this.identityProvider = identityProvider;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public OrganizationRegistrationResult complete(CompleteOrganizationRegistrationCommand command) {
		AuthenticatedIdentity identity = identityProvider.currentIdentity();

		if (userRepository.findByClerkUserId(identity.clerkUserId()).isPresent()) {
			throw new UserAlreadyRegisteredException();
		}

		User user = User.createOrganization(
				UUID.randomUUID(),
				identity.clerkUserId(),
				command.organizationName(),
				identity.email(),
				command.cnpj());

		User savedUser = userRepository.save(user);
		return new OrganizationRegistrationResult(
				savedUser.getId(),
				savedUser.getPersonType(),
				savedUser.getCurrentRole(),
				savedUser.getOrganizationName(),
				savedUser.getCnpj(),
				savedUser.getEmail());
	}
}
