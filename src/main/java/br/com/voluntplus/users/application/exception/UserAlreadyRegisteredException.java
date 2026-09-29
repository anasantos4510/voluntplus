package br.com.voluntplus.users.application.exception;

public class UserAlreadyRegisteredException extends RuntimeException {

	public UserAlreadyRegisteredException() {
		super("The authenticated identity already has a VoluntPlus user");
	}

	public UserAlreadyRegisteredException(Throwable cause) {
		super("The authenticated identity already has a VoluntPlus user", cause);
	}
}
