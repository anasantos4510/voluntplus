package br.com.voluntplus.volunteerservices.application.exception;

public class ManagedVolunteerServiceNotFoundException extends RuntimeException {

	public ManagedVolunteerServiceNotFoundException() {
		super("The volunteer service was not found");
	}
}
