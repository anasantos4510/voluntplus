package br.com.voluntplus.volunteerservices.application.service;

import br.com.voluntplus.users.api.CurrentRole;
import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.volunteerservices.application.port.in.CreateVolunteerServiceUseCase;
import br.com.voluntplus.volunteerservices.application.port.out.VolunteerServiceRepository;
import br.com.voluntplus.volunteerservices.domain.model.Availability;
import br.com.voluntplus.volunteerservices.domain.model.Location;
import br.com.voluntplus.volunteerservices.domain.model.ServiceContacts;
import br.com.voluntplus.volunteerservices.domain.model.ServiceImage;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class CreateVolunteerServiceService implements CreateVolunteerServiceUseCase {

	private final UsersApi usersApi;
	private final VolunteerServiceRepository repository;
	private final Clock clock;

	public CreateVolunteerServiceService(
			UsersApi usersApi,
			VolunteerServiceRepository repository,
			Clock clock) {
		this.usersApi = usersApi;
		this.repository = repository;
		this.clock = clock;
	}

	@Override
	@Transactional
	public CreateVolunteerServiceResult create(CreateVolunteerServiceCommand command) {
		UserSummary owner = usersApi.requireCurrentUserWithRole(CurrentRole.OFFERER);
		Instant now = Instant.now(clock);

		VolunteerService service = VolunteerService.create(
				UUID.randomUUID(),
				owner.userId(),
				command.name(),
				command.description(),
				command.category(),
				command.modality(),
				toLocation(command.location()),
				toAvailability(command.availability()),
				toContacts(command.contacts()),
				toImage(command.imageDataUrl()),
				now);

		VolunteerService savedService = repository.save(service);
		return new CreateVolunteerServiceResult(savedService, owner);
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
}
