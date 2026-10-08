package br.com.voluntplus.reviews.application.port.in;

import br.com.voluntplus.reviews.domain.model.Review;

public record ReviewResult(Review review, String authorName) {
}
