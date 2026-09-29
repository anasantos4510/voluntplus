package br.com.voluntplus.users.domain.exception;

import br.com.voluntplus.users.domain.model.UserRole;

public class UserRoleAlreadyActiveException extends RuntimeException {

	public UserRoleAlreadyActiveException(UserRole role) {
		super("The user already has the " + role + " role");
	}
}
