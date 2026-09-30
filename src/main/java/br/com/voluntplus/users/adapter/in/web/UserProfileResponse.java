package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserProfileResponse(
		UUID id,
		PersonType personType,
		String fullName,
		String organizationName,
		String email,
		LocalDate birthDate,
		Gender gender,
		UserRole currentRole) {
}
