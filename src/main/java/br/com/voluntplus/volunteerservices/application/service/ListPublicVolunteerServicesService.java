package br.com.voluntplus.volunteerservices.application.service;

import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.volunteerservices.application.port.in.ListPublicVolunteerServicesUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.PublicVolunteerServiceResult;
import br.com.voluntplus.volunteerservices.application.port.out.VolunteerServiceRepository;
import br.com.voluntplus.volunteerservices.api.ServiceReviewStatistics;
import br.com.voluntplus.volunteerservices.api.ServiceReviewStatisticsProvider;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListPublicVolunteerServicesService implements ListPublicVolunteerServicesUseCase {

	private final VolunteerServiceRepository repository;
	private final UsersApi usersApi;
	private final ServiceReviewStatisticsProvider reviewStatisticsProvider;

	public ListPublicVolunteerServicesService(
			VolunteerServiceRepository repository,
			UsersApi usersApi,
			ServiceReviewStatisticsProvider reviewStatisticsProvider) {
		this.repository = repository;
		this.usersApi = usersApi;
		this.reviewStatisticsProvider = reviewStatisticsProvider;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PublicVolunteerServiceResult> listPublicServices() {
		List<VolunteerService> services = repository.findAllActiveNotDeleted();
		if (services.isEmpty()) {
			return List.of();
		}

		Set<UUID> ownerIds = services.stream()
				.map(VolunteerService::getOwnerId)
				.collect(Collectors.toUnmodifiableSet());
		Map<UUID, UserSummary> owners = usersApi.findAllByIds(ownerIds);
		Set<UUID> serviceIds = services.stream()
				.map(VolunteerService::getId)
				.collect(Collectors.toUnmodifiableSet());
		Map<UUID, ServiceReviewStatistics> reviewStatistics =
				reviewStatisticsProvider.findByServiceIds(serviceIds);

		return services.stream()
				.filter(service -> owners.containsKey(service.getOwnerId()))
				.map(service -> new PublicVolunteerServiceResult(
						service,
						owners.get(service.getOwnerId()),
						reviewStatistics.getOrDefault(
								service.getId(),
								ServiceReviewStatistics.empty(service.getId()))))
				.toList();
	}
}
