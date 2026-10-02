package br.com.voluntplus.volunteerservices.domain.exception;

public abstract class VolunteerServiceDomainException extends RuntimeException {

	protected VolunteerServiceDomainException(String message) {
		super(message);
	}

	protected VolunteerServiceDomainException(String message, Throwable cause) {
		super(message, cause);
	}
}
