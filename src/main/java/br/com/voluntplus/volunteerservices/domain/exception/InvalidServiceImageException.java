package br.com.voluntplus.volunteerservices.domain.exception;

public final class InvalidServiceImageException extends VolunteerServiceDomainException {

	public InvalidServiceImageException(String message) {
		super(message);
	}

	public InvalidServiceImageException(String message, Throwable cause) {
		super(message, cause);
	}
}
