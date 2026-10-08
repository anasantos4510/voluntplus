package br.com.voluntplus.volunteerservices.application.service;

import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.volunteerservices.application.exception.PublicVolunteerServiceNotFoundException;
import br.com.voluntplus.volunteerservices.application.port.in.GetPublicVolunteerServiceUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.PublicVolunteerServiceResult;
import br.com.voluntplus.volunteerservices.application.port.out.VolunteerServiceRepository;
import br.com.voluntplus.volunteerservices.api.ServiceReviewStatistics;
import br.com.voluntplus.volunteerservices.api.ServiceReviewStatisticsProvider;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Map;
import java.util.Set;

@Service
public class GetPublicVolunteerServiceService implements GetPublicVolunteerServiceUseCase {

	private final VolunteerServiceRepository repository;
	private final UsersApi usersApi;
	private final ServiceReviewStatisticsProvider reviewStatisticsProvider;

	public GetPublicVolunteerServiceService(
			VolunteerServiceRepository repository,
			UsersApi usersApi,
			ServiceReviewStatisticsProvider reviewStatisticsProvider) {
		this.repository = repository;
		this.usersApi = usersApi;
		this.reviewStatisticsProvider = reviewStatisticsProvider;
	}

	@Override
	@Transactional(readOnly = true)
	public PublicVolunteerServiceResult getPublicService(UUID serviceId) {
		VolunteerService service = repository.findActiveNotDeletedById(serviceId)
				.orElseThrow(PublicVolunteerServiceNotFoundException::new);
		UserSummary owner = usersApi.findById(service.getOwnerId())
				.orElseThrow(PublicVolunteerServiceNotFoundException::new);
		Map<UUID, ServiceReviewStatistics> statistics =
				reviewStatisticsProvider.findByServiceIds(Set.of(serviceId));
		return new PublicVolunteerServiceResult(
				service,
				owner,
				statistics.getOrDefault(serviceId, ServiceReviewStatistics.empty(serviceId)));
	}
}
