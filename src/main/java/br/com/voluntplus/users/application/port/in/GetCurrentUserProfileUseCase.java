package br.com.voluntplus.users.application.port.in;

import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;

import java.time.LocalDate;
import java.util.UUID;

public interface GetCurrentUserProfileUseCase {

	CurrentUserProfileResult getCurrentProfile();

	record CurrentUserProfileResult(
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
