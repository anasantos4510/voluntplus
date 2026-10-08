package br.com.voluntplus.volunteerservices.application.port.in;

import br.com.voluntplus.volunteerservices.domain.model.LocationType;
import br.com.voluntplus.volunteerservices.domain.model.ServiceCategory;
import br.com.voluntplus.volunteerservices.domain.model.ServiceModality;
import br.com.voluntplus.volunteerservices.domain.model.Shift;
import br.com.voluntplus.volunteerservices.domain.model.Weekday;

import java.util.List;
import java.util.UUID;

public interface EditVolunteerServiceUseCase {

	OwnedVolunteerServiceResult edit(UUID serviceId, EditVolunteerServiceCommand command);

	record EditVolunteerServiceCommand(
			String name,
			String description,
			ServiceCategory category,
			ServiceModality modality,
			LocationData location,
			AvailabilityData availability,
			ContactData contacts,
			String imageDataUrl) {
	}

	record LocationData(
			String postalCode,
			String state,
			String city,
			String neighborhood,
			LocationType type) {
	}

	record AvailabilityData(
			List<Weekday> weekdays,
			List<Shift> shifts) {
	}

	record ContactData(
			String whatsapp,
			String phone,
			String instagram,
			String website) {
	}
}
