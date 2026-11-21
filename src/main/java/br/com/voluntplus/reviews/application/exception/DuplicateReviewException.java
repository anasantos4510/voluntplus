package br.com.voluntplus.reviews.application.exception;

public class DuplicateReviewException extends RuntimeException {

	public DuplicateReviewException() {
		super("The beneficiary has already reviewed this volunteer service");
	}

	public DuplicateReviewException(Throwable cause) {
		super("The beneficiary has already reviewed this volunteer service", cause);
	}
}
