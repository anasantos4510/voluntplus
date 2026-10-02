package br.com.voluntplus.volunteerservices.domain.exception;

import br.com.voluntplus.volunteerservices.domain.model.ServiceStatus;

public final class ServiceStatusAlreadySetException extends VolunteerServiceDomainException {

	public ServiceStatusAlreadySetException(ServiceStatus status) {
		super("Volunteer service already has status " + status);
	}
}
