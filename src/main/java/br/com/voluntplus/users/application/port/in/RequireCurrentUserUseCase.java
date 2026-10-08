package br.com.voluntplus.users.application.port.in;

import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.UserRole;

import java.time.LocalDate;
import java.util.UUID;

public interface RequireCurrentUserUseCase {

	CurrentUser requireCurrentUser();

	record CurrentUser(
			UUID userId,
			PersonType personType,
			UserRole currentRole,
			String email,
			String fullName,
			String organizationName,
			LocalDate birthDate,
			Gender gender) {
	}
}
