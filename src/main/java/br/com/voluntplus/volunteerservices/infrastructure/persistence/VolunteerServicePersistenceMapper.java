package br.com.voluntplus.volunteerservices.infrastructure.persistence;

import br.com.voluntplus.volunteerservices.domain.model.Availability;
import br.com.voluntplus.volunteerservices.domain.model.Location;
import br.com.voluntplus.volunteerservices.domain.model.LocationType;
import br.com.voluntplus.volunteerservices.domain.model.ServiceContacts;
import br.com.voluntplus.volunteerservices.domain.model.ServiceImage;
import br.com.voluntplus.volunteerservices.domain.model.Shift;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import br.com.voluntplus.volunteerservices.domain.model.Weekday;

import java.util.Set;

final class VolunteerServicePersistenceMapper {

	private VolunteerServicePersistenceMapper() {
	}

	static VolunteerServiceJpaEntity toEntity(VolunteerService service) {
		Location location = service.getLocation();
		ServiceContacts contacts = service.getContacts();
		Availability availability = service.getAvailability();
		ServiceImage image = service.getImage();

		return new VolunteerServiceJpaEntity(
				service.getId(),
				service.getOwnerId(),
				service.getName(),
				service.getDescription(),
				service.getCategory(),
				service.getModality(),
				service.getStatus(),
				location == null ? null : location.getPostalCode(),
				location == null ? null : location.getState(),
				location == null ? null : location.getCity(),
				location == null ? null : location.getNeighborhood(),
				location == null ? null : location.getType().getCode(),
				contacts.getWhatsapp(),
				contacts.getPhone(),
				contacts.getInstagram(),
				contacts.getWebsite(),
				image == null ? null : image.getDataUrl(),
				service.getCreatedAt(),
				service.getUpdatedAt(),
				service.getDeletedAt(),
				availability == null ? Set.of() : availability.getWeekdays(),
				availability == null ? Set.of() : availability.getShifts());
	}

	static VolunteerService toDomain(VolunteerServiceJpaEntity entity) {
		Location location = entity.getLocationType() == null
				? null
				: new Location(
						entity.getPostalCode(),
						entity.getState(),
						entity.getCity(),
						entity.getNeighborhood(),
						LocationType.fromCode(entity.getLocationType()));

		Availability availability = restoreAvailability(entity.getWeekdays(), entity.getShifts());
		ServiceContacts contacts = new ServiceContacts(
				entity.getWhatsapp(),
				entity.getPhone(),
				entity.getInstagram(),
				entity.getWebsite());
		ServiceImage image = entity.getImageDataUrl() == null
				? null
				: new ServiceImage(entity.getImageDataUrl());

		return VolunteerService.restore(
				entity.getId(),
				entity.getOwnerId(),
				entity.getName(),
				entity.getDescription(),
				entity.getCategory(),
				entity.getModality(),
				entity.getStatus(),
				location,
				availability,
				contacts,
				image,
				entity.getCreatedAt(),
				entity.getUpdatedAt(),
				entity.getDeletedAt());
	}

	private static Availability restoreAvailability(Set<Weekday> weekdays, Set<Shift> shifts) {
		if (weekdays.isEmpty() && shifts.isEmpty()) {
			return null;
		}
		return new Availability(weekdays, shifts);
	}
}
