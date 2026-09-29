package br.com.voluntplus.users.application.exception;

public class UserProfileNotFoundException extends RuntimeException {

	public UserProfileNotFoundException() {
		super("No VoluntPlus profile was found for the authenticated identity");
	}
}
