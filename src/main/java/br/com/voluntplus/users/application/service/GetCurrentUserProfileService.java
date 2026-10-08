package br.com.voluntplus.users.application.service;

import br.com.voluntplus.users.application.exception.UserProfileNotFoundException;
import br.com.voluntplus.users.application.port.in.GetCurrentUserProfileUseCase;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCurrentUserProfileService implements GetCurrentUserProfileUseCase {

	private final AuthenticatedIdentityProvider identityProvider;
	private final UserRepository userRepository;

	public GetCurrentUserProfileService(
			AuthenticatedIdentityProvider identityProvider,
			UserRepository userRepository) {
		this.identityProvider = identityProvider;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public CurrentUserProfileResult getCurrentProfile() {
		String clerkUserId = identityProvider.currentClerkUserId();
		User user = userRepository.findByClerkUserId(clerkUserId)
				.orElseThrow(UserProfileNotFoundException::new);

		return new CurrentUserProfileResult(
				user.getId(),
				user.getPersonType(),
				user.getFullName(),
				user.getOrganizationName(),
				user.getCnpj(),
				user.getEmail(),
				user.getBirthDate(),
				user.getGender(),
				user.getCurrentRole());
	}
}
