package br.com.voluntplus.reviews.application.exception;

import java.util.UUID;

public class ReviewableServiceNotFoundException extends RuntimeException {

	public ReviewableServiceNotFoundException(UUID serviceId) {
		super("Volunteer service not found or unavailable for reviews: " + serviceId);
	}
}
