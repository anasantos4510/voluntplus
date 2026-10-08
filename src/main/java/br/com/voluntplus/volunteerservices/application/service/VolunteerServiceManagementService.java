package br.com.voluntplus.volunteerservices.application.service;

import br.com.voluntplus.users.api.CurrentRole;
import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.volunteerservices.application.exception.ManagedVolunteerServiceNotFoundException;
import br.com.voluntplus.volunteerservices.application.port.in.ChangeVolunteerServiceStatusUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.DeleteVolunteerServiceUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.EditVolunteerServiceUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.OwnedVolunteerServiceResult;
import br.com.voluntplus.volunteerservices.application.port.out.VolunteerServiceRepository;
import br.com.voluntplus.volunteerservices.domain.model.Availability;
import br.com.voluntplus.volunteerservices.domain.model.Location;
import br.com.voluntplus.volunteerservices.domain.model.ServiceContacts;
import br.com.voluntplus.volunteerservices.domain.model.ServiceImage;
import br.com.voluntplus.volunteerservices.domain.model.ServiceStatus;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class VolunteerServiceManagementService implements
		EditVolunteerServiceUseCase,
		ChangeVolunteerServiceStatusUseCase,
		DeleteVolunteerServiceUseCase {

	private final UsersApi usersApi;
	private final VolunteerServiceRepository repository;
	private final Clock clock;

	public VolunteerServiceManagementService(
			UsersApi usersApi,
			VolunteerServiceRepository repository,
			Clock clock) {
		this.usersApi = usersApi;
		this.repository = repository;
		this.clock = clock;
	}

	@Override
	@Transactional
	public OwnedVolunteerServiceResult edit(UUID serviceId, EditVolunteerServiceCommand command) {
		OwnedService ownedService = requireOwnedService(serviceId);
		VolunteerService service = ownedService.service();
		service.edit(
				command.name(),
				command.description(),
				command.category(),
				command.modality(),
				toLocation(command.location()),
				toAvailability(command.availability()),
				toContacts(command.contacts()),
				toImage(command.imageDataUrl()),
				Instant.now(clock));

		return new OwnedVolunteerServiceResult(repository.save(service), ownedService.owner());
	}

	@Override
	@Transactional
	public OwnedVolunteerServiceResult changeStatus(UUID serviceId, ServiceStatus requestedStatus) {
		OwnedService ownedService = requireOwnedService(serviceId);
		VolunteerService service = ownedService.service();
		Instant now = Instant.now(clock);

		switch (requestedStatus) {
			case ATIVO -> service.activate(now);
			case INATIVO -> service.inactivate(now);
		}

		return new OwnedVolunteerServiceResult(repository.save(service), ownedService.owner());
	}

	@Override
	@Transactional
	public void delete(UUID serviceId) {
		OwnedService ownedService = requireOwnedService(serviceId);
		ownedService.service().delete(Instant.now(clock));
		repository.save(ownedService.service());
	}

	private OwnedService requireOwnedService(UUID serviceId) {
		UserSummary owner = usersApi.requireCurrentUserWithRole(CurrentRole.OFFERER);
		VolunteerService service = repository.findOwnedNotDeletedById(serviceId, owner.userId())
				.orElseThrow(ManagedVolunteerServiceNotFoundException::new);
		return new OwnedService(service, owner);
	}

	private Location toLocation(LocationData data) {
		return data == null
				? null
				: new Location(data.postalCode(), data.state(), data.city(), data.neighborhood(), data.type());
	}

	private Availability toAvailability(AvailabilityData data) {
		return data == null ? null : new Availability(data.weekdays(), data.shifts());
	}

	private ServiceContacts toContacts(ContactData data) {
		if (data == null) {
			return new ServiceContacts(null, null, null, null);
		}
		return new ServiceContacts(data.whatsapp(), data.phone(), data.instagram(), data.website());
	}

	private ServiceImage toImage(String dataUrl) {
		return dataUrl == null || dataUrl.isBlank() ? null : new ServiceImage(dataUrl);
	}

	private record OwnedService(VolunteerService service, UserSummary owner) {
	}
}
