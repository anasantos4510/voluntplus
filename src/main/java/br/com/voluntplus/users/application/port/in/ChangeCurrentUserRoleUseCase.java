package br.com.voluntplus.users.application.port.in;

import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;

import java.util.UUID;

public interface ChangeCurrentUserRoleUseCase {

	ChangeCurrentUserRoleResult change(ChangeCurrentUserRoleCommand command);

	record ChangeCurrentUserRoleCommand(UserRole role) {
	}

	record ChangeCurrentUserRoleResult(
			UUID userId,
			PersonType personType,
			UserRole currentRole) {
	}
}
