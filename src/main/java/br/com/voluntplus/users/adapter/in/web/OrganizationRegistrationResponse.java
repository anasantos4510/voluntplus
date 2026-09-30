package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;

import java.util.UUID;

public record OrganizationRegistrationResponse(
		UUID id,
		PersonType personType,
		UserRole currentRole,
		String organizationName,
		String cnpj,
		String email) {
}
