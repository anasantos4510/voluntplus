package br.com.voluntplus.volunteerservices.adapter.in.web;

import br.com.voluntplus.users.api.PersonType;
import br.com.voluntplus.users.api.PublicGender;
import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.volunteerservices.adapter.in.web.VolunteerServiceResponse.CategoryResponse;
import br.com.voluntplus.volunteerservices.adapter.in.web.VolunteerServiceResponse.ContactResponse;
import br.com.voluntplus.volunteerservices.adapter.in.web.VolunteerServiceResponse.LocationResponse;
import br.com.voluntplus.volunteerservices.adapter.in.web.VolunteerServiceResponse.ScheduleResponse;
import br.com.voluntplus.volunteerservices.adapter.in.web.VolunteerServiceResponse.UserResponse;
import br.com.voluntplus.volunteerservices.domain.model.Availability;
import br.com.voluntplus.volunteerservices.api.ServiceReviewStatistics;
import br.com.voluntplus.volunteerservices.domain.model.Location;
import br.com.voluntplus.volunteerservices.domain.model.ServiceCategory;
import br.com.voluntplus.volunteerservices.domain.model.ServiceContacts;
import br.com.voluntplus.volunteerservices.domain.model.Shift;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import br.com.voluntplus.volunteerservices.domain.model.Weekday;

import java.util.Comparator;
import java.util.List;

final class VolunteerServiceResponseMapper {

	private VolunteerServiceResponseMapper() {
	}

	static VolunteerServiceResponse toResponse(VolunteerService service, UserSummary owner) {
		return toResponse(service, owner, ServiceReviewStatistics.empty(service.getId()));
	}

	static VolunteerServiceResponse toResponse(
			VolunteerService service,
			UserSummary owner,
			ServiceReviewStatistics reviewStatistics) {
		Location location = service.getLocation();
		Availability availability = service.getAvailability();
		ServiceContacts contacts = service.getContacts();
		CategoryResponse category = toCategory(service.getCategory());
		List<Weekday> weekdays = sortedWeekdays(availability);
		List<Shift> shifts = sortedShifts(availability);

		return new VolunteerServiceResponse(
				service.getId(),
				service.getOwnerId(),
				service.getName(),
				service.getDescription(),
				service.getModality().name(),
				category.id(),
				category,
				service.getStatus().name(),
				reviewStatistics.averageRating(),
				reviewStatistics.reviewCount(),
				service.getImage() == null ? null : service.getImage().getDataUrl(),
				service.getCreatedAt(),
				service.getUpdatedAt(),
				location == null ? null : location.getPostalCode(),
				location == null ? null : location.getState(),
				location == null ? null : location.getCity(),
				location == null ? null : location.getNeighborhood(),
				location == null ? null : location.getType().getCode(),
				toLocation(location),
				weekdays.stream().map(Enum::name).toList(),
				shifts.stream().map(Enum::name).toList(),
				toSchedules(weekdays, shifts),
				contacts.getWhatsapp(),
				contacts.getPhone(),
				contacts.getInstagram(),
				contacts.getWebsite(),
				new ContactResponse(
						contacts.getWhatsapp() != null ? contacts.getWhatsapp() : contacts.getPhone(),
						contacts.getInstagram(),
						contacts.getWebsite()),
				toUser(owner));
	}

	private static CategoryResponse toCategory(ServiceCategory category) {
		return switch (category) {
			case EDUCACAO -> new CategoryResponse("EDUCACAO", "Educação");
			case MUSICA -> new CategoryResponse("MUSICA", "Música");
			case TECNOLOGIA -> new CategoryResponse("TECNOLOGIA", "Tecnologia");
			case ESPORTE -> new CategoryResponse("ESPORTE", "Esporte");
			case ALIMENTACAO -> new CategoryResponse("ALIMENTAÇÃO", "Alimentação");
			case DOACOES -> new CategoryResponse("DOACOES", "Doações");
			case SAUDE -> new CategoryResponse("SAUDE", "Saúde");
			case ANIMAIS -> new CategoryResponse("ANIMAIS", "Animais");
			case GERAIS -> new CategoryResponse("GERAIS", "Serviços gerais");
			case APOIO_COMUNITARIO -> new CategoryResponse("APOIO_COMUNITARIO", "Apoio comunitário");
			case OUTROS -> new CategoryResponse("OUTROS", "Outros");
		};
	}

	private static LocationResponse toLocation(Location location) {
		return location == null
				? null
				: new LocationResponse(
						location.getPostalCode(),
						location.getState(),
						location.getCity(),
						location.getNeighborhood(),
						location.getType().getCode());
	}

	private static List<Weekday> sortedWeekdays(Availability availability) {
		return availability == null
				? List.of()
				: availability.getWeekdays().stream()
						.sorted(Comparator.comparingInt(Enum::ordinal))
						.toList();
	}

	private static List<Shift> sortedShifts(Availability availability) {
		return availability == null
				? List.of()
				: availability.getShifts().stream()
						.sorted(Comparator.comparingInt(Enum::ordinal))
						.toList();
	}

	private static List<ScheduleResponse> toSchedules(List<Weekday> weekdays, List<Shift> shifts) {
		return weekdays.stream()
				.flatMap(weekday -> shifts.stream()
						.map(shift -> new ScheduleResponse(weekday.name(), shift.name())))
				.toList();
	}

	private static UserResponse toUser(UserSummary owner) {
		return new UserResponse(
				owner.userId(),
				owner.fullName(),
				owner.organizationName(),
				owner.email(),
				owner.personType() == PersonType.INDIVIDUAL ? "PF" : "PJ",
				toFrontendGender(owner.gender()),
				owner.age());
	}

	private static String toFrontendGender(PublicGender gender) {
		if (gender == null) {
			return null;
		}
		return switch (gender) {
			case FEMALE -> "F";
			case MALE -> "M";
			case NON_BINARY, OTHER, PREFER_NOT_TO_SAY -> "O";
		};
	}
}
