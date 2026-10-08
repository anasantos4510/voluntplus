package br.com.voluntplus.volunteerservices.application.port.in;

import java.util.UUID;

public interface GetPublicVolunteerServiceUseCase {

	PublicVolunteerServiceResult getPublicService(UUID serviceId);
}
