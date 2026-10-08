package br.com.voluntplus.volunteerservices.application.port.in;

import java.util.List;

public interface ListOwnedVolunteerServicesUseCase {

	List<OwnedVolunteerServiceResult> listOwnedServices();
}
