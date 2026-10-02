package br.com.voluntplus.volunteerservices.application.port.in;

import java.util.UUID;

public interface DeleteVolunteerServiceUseCase {

	void delete(UUID serviceId);
}
