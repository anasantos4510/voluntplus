package br.com.voluntplus.reviews.application.exception;

public class OwnServiceReviewNotAllowedException extends RuntimeException {

	public OwnServiceReviewNotAllowedException() {
		super("The owner cannot review their own volunteer service");
	}
}
