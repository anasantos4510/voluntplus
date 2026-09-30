package br.com.voluntplus.users.application.port.in;

import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;

import java.time.LocalDate;
import java.util.UUID;

public interface CompleteIndividualRegistrationUseCase {

	IndividualRegistrationResult complete(CompleteIndividualRegistrationCommand command);

	record CompleteIndividualRegistrationCommand(
			String fullName,
			LocalDate birthDate,
			Gender gender,
			UserRole initialRole) {
	}

	record IndividualRegistrationResult(
			UUID id,
			PersonType personType,
			UserRole currentRole,
			String fullName,
			LocalDate birthDate,
			Gender gender,
			String email) {
	}
}
