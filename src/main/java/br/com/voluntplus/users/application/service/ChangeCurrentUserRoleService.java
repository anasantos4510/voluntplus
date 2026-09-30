package br.com.voluntplus.users.application.service;

import br.com.voluntplus.users.application.exception.UserProfileNotFoundException;
import br.com.voluntplus.users.application.port.in.ChangeCurrentUserRoleUseCase;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeCurrentUserRoleService implements ChangeCurrentUserRoleUseCase {

	private final AuthenticatedIdentityProvider identityProvider;
	private final UserRepository userRepository;

	public ChangeCurrentUserRoleService(
			AuthenticatedIdentityProvider identityProvider,
			UserRepository userRepository) {
		this.identityProvider = identityProvider;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public ChangeCurrentUserRoleResult change(ChangeCurrentUserRoleCommand command) {
		String clerkUserId = identityProvider.currentClerkUserId();
		User user = userRepository.findByClerkUserId(clerkUserId)
				.orElseThrow(UserProfileNotFoundException::new);

		user.changeCurrentRole(command.role());
		User savedUser = userRepository.save(user);

		return new ChangeCurrentUserRoleResult(
				savedUser.getId(),
				savedUser.getPersonType(),
				savedUser.getCurrentRole());
	}
}
