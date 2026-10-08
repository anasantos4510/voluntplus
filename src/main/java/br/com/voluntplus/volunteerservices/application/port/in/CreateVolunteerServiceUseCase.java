package br.com.voluntplus.volunteerservices.application.port.in;

import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.volunteerservices.domain.model.LocationType;
import br.com.voluntplus.volunteerservices.domain.model.ServiceCategory;
import br.com.voluntplus.volunteerservices.domain.model.ServiceModality;
import br.com.voluntplus.volunteerservices.domain.model.Shift;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import br.com.voluntplus.volunteerservices.domain.model.Weekday;

import java.util.List;

public interface CreateVolunteerServiceUseCase {

	CreateVolunteerServiceResult create(CreateVolunteerServiceCommand command);

	record CreateVolunteerServiceCommand(
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

	record CreateVolunteerServiceResult(
			VolunteerService service,
			UserSummary owner) {
	}
}
