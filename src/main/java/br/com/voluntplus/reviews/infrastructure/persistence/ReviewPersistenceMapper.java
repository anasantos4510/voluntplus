package br.com.voluntplus.reviews.infrastructure.persistence;

import br.com.voluntplus.reviews.domain.model.Review;

final class ReviewPersistenceMapper {

	private ReviewPersistenceMapper() {
	}

	static ReviewJpaEntity toEntity(Review review) {
		return new ReviewJpaEntity(
				review.getId(),
				review.getServiceId(),
				review.getAuthorId(),
				review.getRating(),
				review.getComment(),
				review.getCreatedAt());
	}

	static Review toDomain(ReviewJpaEntity entity) {
		return Review.restore(
				entity.getId(),
				entity.getServiceId(),
				entity.getAuthorId(),
				entity.getRating(),
				entity.getComment(),
				entity.getCreatedAt());
	}
}
