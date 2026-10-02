package br.com.voluntplus.volunteerservices.application.port.in;

import br.com.voluntplus.volunteerservices.domain.model.ServiceStatus;

import java.util.UUID;

public interface ChangeVolunteerServiceStatusUseCase {

	OwnedVolunteerServiceResult changeStatus(UUID serviceId, ServiceStatus requestedStatus);
}
