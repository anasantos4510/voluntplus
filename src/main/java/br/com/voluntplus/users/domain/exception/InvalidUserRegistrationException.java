package br.com.voluntplus.users.domain.exception;

public class InvalidUserRegistrationException extends IllegalArgumentException {

	public InvalidUserRegistrationException(String message) {
		super(message);
	}
}
