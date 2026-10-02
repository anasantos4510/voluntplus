package br.com.voluntplus.users.api;

public class CurrentUserUnavailableException extends RuntimeException {

	public CurrentUserUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
