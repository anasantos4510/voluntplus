package br.com.voluntplus.users.application.port.in;

import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;

import java.time.LocalDate;
import java.util.UUID;

public interface UpdateCurrentUserProfileUseCase {

	UpdateCurrentUserProfileResult update(UpdateCurrentUserProfileCommand command);

	record UpdateCurrentUserProfileCommand(
			String fullName,
			LocalDate birthDate,
			Gender gender,
			String organizationName,
			String cnpj) {
	}

	record UpdateCurrentUserProfileResult(
			UUID id,
			PersonType personType,
			String fullName,
			String organizationName,
			String cnpj,
			String email,
			LocalDate birthDate,
			Gender gender,
			UserRole currentRole) {
	}
}
