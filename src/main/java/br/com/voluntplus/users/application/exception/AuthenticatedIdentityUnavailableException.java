package br.com.voluntplus.users.application.exception;

public class AuthenticatedIdentityUnavailableException extends RuntimeException {

	public AuthenticatedIdentityUnavailableException(String message) {
		super(message);
	}
}
