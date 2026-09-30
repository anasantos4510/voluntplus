package br.com.voluntplus.users.application.port.in;

import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;

import java.util.UUID;

public interface CompleteOrganizationRegistrationUseCase {

	OrganizationRegistrationResult complete(CompleteOrganizationRegistrationCommand command);

	record CompleteOrganizationRegistrationCommand(
			String organizationName,
			String cnpj) {
	}

	record OrganizationRegistrationResult(
			UUID id,
			PersonType personType,
			UserRole currentRole,
			String organizationName,
			String cnpj,
			String email) {
	}
}
