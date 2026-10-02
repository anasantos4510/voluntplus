package br.com.voluntplus.users.domain.exception;

public class UserRoleChangeNotAllowedException extends RuntimeException {

	public UserRoleChangeNotAllowedException(String message) {
		super(message);
	}
}
