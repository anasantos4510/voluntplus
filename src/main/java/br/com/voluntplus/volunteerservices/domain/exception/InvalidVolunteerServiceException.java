package br.com.voluntplus.volunteerservices.domain.exception;

public final class InvalidVolunteerServiceException extends VolunteerServiceDomainException {

	public InvalidVolunteerServiceException(String message) {
		super(message);
	}

	public InvalidVolunteerServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
