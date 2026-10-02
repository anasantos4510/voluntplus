package br.com.voluntplus.volunteerservices.api;

import java.util.Optional;
import java.util.UUID;

public interface VolunteerServicesApi {

	Optional<ServiceReviewContext> findReviewContext(UUID serviceId);
}
