package br.com.voluntplus.users.domain.exception;

public class InvalidUserProfileUpdateException extends RuntimeException {

	public InvalidUserProfileUpdateException(String message) {
		super(message);
	}
}
