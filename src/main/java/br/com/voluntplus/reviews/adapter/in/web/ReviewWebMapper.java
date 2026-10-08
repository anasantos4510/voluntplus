package br.com.voluntplus.reviews.adapter.in.web;

import br.com.voluntplus.reviews.application.port.in.RegisterReviewUseCase.RegisterReviewCommand;
import br.com.voluntplus.reviews.application.port.in.ReviewResult;
import br.com.voluntplus.reviews.domain.model.Review;

import java.util.UUID;

final class ReviewWebMapper {

	private ReviewWebMapper() {
	}

	static RegisterReviewCommand toCommand(UUID serviceId, CreateReviewRequest request) {
		if (request == null) {
			throw new InvalidReviewRequestException("The request body is required");
		}
		if (request.nota() == null) {
			throw new InvalidReviewRequestException("nota is required");
		}
		return new RegisterReviewCommand(serviceId, request.nota(), request.comentario());
	}

	static ReviewResponse toResponse(ReviewResult result) {
		Review review = result.review();
		return new ReviewResponse(
				review.getId(),
				review.getServiceId(),
				review.getAuthorId(),
				result.authorName(),
				review.getRating(),
				review.getComment(),
				review.getCreatedAt());
	}
}
