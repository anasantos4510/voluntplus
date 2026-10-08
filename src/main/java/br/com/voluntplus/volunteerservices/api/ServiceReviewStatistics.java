package br.com.voluntplus.volunteerservices.api;

import java.util.Objects;
import java.util.UUID;

public record ServiceReviewStatistics(
		UUID serviceId,
		double averageRating,
		long reviewCount) {

	public ServiceReviewStatistics {
		Objects.requireNonNull(serviceId, "serviceId must not be null");
		if (reviewCount < 0) {
			throw new IllegalArgumentException("reviewCount must not be negative");
		}
		if (averageRating < 0 || averageRating > 5) {
			throw new IllegalArgumentException("averageRating must be between 0 and 5");
		}
	}

	public static ServiceReviewStatistics empty(UUID serviceId) {
		return new ServiceReviewStatistics(serviceId, 0, 0);
	}
}
