package br.com.voluntplus.volunteerservices.application.service;

import br.com.voluntplus.volunteerservices.api.ServiceReviewContext;
import br.com.voluntplus.volunteerservices.api.VolunteerServicesApi;
import br.com.voluntplus.volunteerservices.application.port.out.VolunteerServiceRepository;
import br.com.voluntplus.volunteerservices.domain.model.ServiceStatus;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class VolunteerServicesApiService implements VolunteerServicesApi {

	private final VolunteerServiceRepository repository;

	public VolunteerServicesApiService(VolunteerServiceRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ServiceReviewContext> findReviewContext(UUID serviceId) {
		return repository.findById(serviceId)
				.map(this::toReviewContext);
	}

	private ServiceReviewContext toReviewContext(VolunteerService service) {
		return new ServiceReviewContext(
				service.getId(),
				service.getOwnerId(),
				service.getStatus() == ServiceStatus.ATIVO,
				service.isDeleted());
	}
}
