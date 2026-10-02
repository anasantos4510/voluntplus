package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.domain.model.UserRole;
import jakarta.validation.constraints.NotNull;

public record ChangeCurrentUserRoleRequest(
		@NotNull UserRole role) {
}
