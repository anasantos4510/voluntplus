package br.com.voluntplus.reviews.application.port.in;

import java.util.UUID;

public interface RegisterReviewUseCase {

	ReviewResult register(RegisterReviewCommand command);

	record RegisterReviewCommand(UUID serviceId, int rating, String comment) {
	}
}
