package br.com.voluntplus.reviews.domain.model;

import br.com.voluntplus.reviews.domain.exception.InvalidReviewException;

import java.time.Instant;
import java.util.UUID;

public final class Review {

	private static final int MIN_RATING = 1;
	private static final int MAX_RATING = 5;
	private static final int MAX_COMMENT_LENGTH = 500;

	private final UUID id;
	private final UUID serviceId;
	private final UUID authorId;
	private final int rating;
	private final String comment;
	private final Instant createdAt;

	private Review(
			UUID id,
			UUID serviceId,
			UUID authorId,
			int rating,
			String comment,
			Instant createdAt) {
		this.id = requireNonNull(id, "id");
		this.serviceId = requireNonNull(serviceId, "serviceId");
		this.authorId = requireNonNull(authorId, "authorId");
		this.rating = validateRating(rating);
		this.comment = normalizeComment(comment);
		this.createdAt = requireNonNull(createdAt, "createdAt");
	}

	public static Review create(
			UUID id,
			UUID serviceId,
			UUID authorId,
			int rating,
			String comment,
			Instant createdAt) {
		return new Review(id, serviceId, authorId, rating, comment, createdAt);
	}

	public static Review restore(
			UUID id,
			UUID serviceId,
			UUID authorId,
			int rating,
			String comment,
			Instant createdAt) {
		return new Review(id, serviceId, authorId, rating, comment, createdAt);
	}

	public UUID getId() {
		return id;
	}

	public UUID getServiceId() {
		return serviceId;
	}

	public UUID getAuthorId() {
		return authorId;
	}

	public int getRating() {
		return rating;
	}

	public String getComment() {
		return comment;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	private static int validateRating(int rating) {
		if (rating < MIN_RATING || rating > MAX_RATING) {
			throw new InvalidReviewException("rating must be between 1 and 5");
		}
		return rating;
	}

	private static String normalizeComment(String comment) {
		if (comment == null || comment.isBlank()) {
			return null;
		}
		String normalized = comment.strip();
		if (normalized.length() > MAX_COMMENT_LENGTH) {
			throw new InvalidReviewException("comment must contain at most 500 characters");
		}
		return normalized;
	}

	private static <T> T requireNonNull(T value, String fieldName) {
		if (value == null) {
			throw new InvalidReviewException(fieldName + " must not be null");
		}
		return value;
	}
}
