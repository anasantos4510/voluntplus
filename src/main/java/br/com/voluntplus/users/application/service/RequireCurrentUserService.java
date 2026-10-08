package br.com.voluntplus.users.application.service;

import br.com.voluntplus.users.application.exception.UserProfileNotFoundException;
import br.com.voluntplus.users.application.port.in.RequireCurrentUserUseCase;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequireCurrentUserService implements RequireCurrentUserUseCase {

	private final AuthenticatedIdentityProvider identityProvider;
	private final UserRepository userRepository;

	public RequireCurrentUserService(
			AuthenticatedIdentityProvider identityProvider,
			UserRepository userRepository) {
		this.identityProvider = identityProvider;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public CurrentUser requireCurrentUser() {
		String clerkUserId = identityProvider.currentClerkUserId();
		User user = userRepository.findByClerkUserId(clerkUserId)
				.orElseThrow(UserProfileNotFoundException::new);

		return new CurrentUser(
				user.getId(),
				user.getPersonType(),
				user.getCurrentRole(),
				user.getEmail(),
				user.getFullName(),
				user.getOrganizationName(),
				user.getBirthDate(),
				user.getGender());
	}
}
