package br.com.voluntplus.volunteerservices.api;

import java.util.UUID;

public record ServiceReviewContext(
		UUID serviceId,
		UUID ownerId,
		boolean active,
		boolean deleted) {
}
