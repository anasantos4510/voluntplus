package br.com.voluntplus.reviews.domain.exception;

public class InvalidReviewException extends RuntimeException {

	public InvalidReviewException(String message) {
		super(message);
	}
}
