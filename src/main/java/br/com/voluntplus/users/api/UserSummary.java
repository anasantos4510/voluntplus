package br.com.voluntplus.users.api;

import java.util.UUID;

public record UserSummary(
		UUID userId,
		PersonType personType,
		CurrentRole currentRole) {
}
