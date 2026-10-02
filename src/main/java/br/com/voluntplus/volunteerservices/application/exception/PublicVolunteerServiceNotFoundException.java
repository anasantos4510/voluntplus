package br.com.voluntplus.volunteerservices.application.exception;

public final class PublicVolunteerServiceNotFoundException extends RuntimeException {

	public PublicVolunteerServiceNotFoundException() {
		super("The volunteer service was not found");
	}
}
