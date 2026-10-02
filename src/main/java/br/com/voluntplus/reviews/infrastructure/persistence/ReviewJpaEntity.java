package br.com.voluntplus.reviews.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reviews")
class ReviewJpaEntity {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "service_id", nullable = false, updatable = false)
	private UUID serviceId;

	@Column(name = "author_id", nullable = false, updatable = false)
	private UUID authorId;

	@Column(name = "rating", nullable = false, updatable = false)
	private int rating;

	@Column(name = "comment", length = 500, updatable = false)
	private String comment;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	protected ReviewJpaEntity() {
	}

	ReviewJpaEntity(
			UUID id,
			UUID serviceId,
			UUID authorId,
			int rating,
			String comment,
			Instant createdAt) {
		this.id = id;
		this.serviceId = serviceId;
		this.authorId = authorId;
		this.rating = rating;
		this.comment = comment;
		this.createdAt = createdAt;
	}

	UUID getId() { return id; }
	UUID getServiceId() { return serviceId; }
	UUID getAuthorId() { return authorId; }
	int getRating() { return rating; }
	String getComment() { return comment; }
	Instant getCreatedAt() { return createdAt; }
}
