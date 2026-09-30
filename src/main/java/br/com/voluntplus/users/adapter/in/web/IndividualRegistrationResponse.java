package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;

import java.time.LocalDate;
import java.util.UUID;

public record IndividualRegistrationResponse(
		UUID id,
		PersonType personType,
		UserRole currentRole,
		String fullName,
		LocalDate birthDate,
		Gender gender,
		String email) {
}
