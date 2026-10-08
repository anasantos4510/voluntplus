package br.com.voluntplus.volunteerservices.adapter.in.web;

import br.com.voluntplus.volunteerservices.application.port.in.CreateVolunteerServiceUseCase.AvailabilityData;
import br.com.voluntplus.volunteerservices.application.port.in.CreateVolunteerServiceUseCase.ContactData;
import br.com.voluntplus.volunteerservices.application.port.in.CreateVolunteerServiceUseCase.CreateVolunteerServiceCommand;
import br.com.voluntplus.volunteerservices.application.port.in.CreateVolunteerServiceUseCase.LocationData;
import br.com.voluntplus.volunteerservices.domain.model.LocationType;
import br.com.voluntplus.volunteerservices.domain.model.ServiceCategory;
import br.com.voluntplus.volunteerservices.domain.model.ServiceModality;
import br.com.voluntplus.volunteerservices.domain.model.Shift;
import br.com.voluntplus.volunteerservices.domain.model.Weekday;

import java.util.List;

final class CreateVolunteerServiceRequestMapper {

	private CreateVolunteerServiceRequestMapper() {
	}

	static CreateVolunteerServiceCommand toCommand(CreateVolunteerServiceRequest request) {
		validateUnexpectedProperties(request);
		validateStatus(request.status());

		ServiceModality modality = parseEnum(request.modalities(), ServiceModality.class, "modalities");
		LocationData location = modality == null || modality == ServiceModality.ONLINE
				? null
				: new LocationData(
						request.cep(),
						request.estado(),
						request.cidade(),
						request.bairro(),
						parseLocationType(request.tipoLocalizacao()));

		return new CreateVolunteerServiceCommand(
				request.name(),
				request.descricao(),
				parseCategory(request.idCategoria()),
				modality,
				location,
				toAvailability(request.diaDaSemana(), request.turno()),
				new ContactData(request.whatsapp(), request.telefone(), request.instagram(), request.site()),
				request.providerImage());
	}

	private static AvailabilityData toAvailability(List<String> weekdays, List<String> shifts) {
		if (weekdays == null && shifts == null) {
			return null;
		}
		return new AvailabilityData(
				parseEnums(weekdays, Weekday.class, "diaDaSemana"),
				parseEnums(shifts, Shift.class, "turno"));
	}

	private static ServiceCategory parseCategory(String value) {
		if (value == null) {
			return null;
		}
		if ("ALIMENTAÇÃO".equals(value)) {
			return ServiceCategory.ALIMENTACAO;
		}
		return parseEnum(value, ServiceCategory.class, "idCategoria");
	}

	private static LocationType parseLocationType(Integer value) {
		if (value == null) {
			return null;
		}
		try {
			return LocationType.fromCode(value);
		} catch (RuntimeException exception) {
			throw new InvalidVolunteerServiceRequestException("tipoLocalizacao contains an unsupported value");
		}
	}

	private static <E extends Enum<E>> List<E> parseEnums(
			List<String> values,
			Class<E> enumType,
			String fieldName) {
		if (values == null) {
			return null;
		}
		return values.stream()
				.map(value -> parseEnum(value, enumType, fieldName))
				.toList();
	}

	private static <E extends Enum<E>> E parseEnum(String value, Class<E> enumType, String fieldName) {
		if (value == null) {
			return null;
		}
		try {
			return Enum.valueOf(enumType, value);
		} catch (IllegalArgumentException exception) {
			throw new InvalidVolunteerServiceRequestException(fieldName + " contains an unsupported value");
		}
	}

	private static void validateStatus(String status) {
		if (status != null && !"ATIVO".equals(status)) {
			throw new InvalidVolunteerServiceRequestException("status must be ATIVO when provided");
		}
	}

	private static void validateUnexpectedProperties(CreateVolunteerServiceRequest request) {
		if (!request.unexpectedProperties().isEmpty()) {
			throw new InvalidVolunteerServiceRequestException(
					"The request contains unsupported properties: "
							+ String.join(", ", request.unexpectedProperties()));
		}
	}
}
