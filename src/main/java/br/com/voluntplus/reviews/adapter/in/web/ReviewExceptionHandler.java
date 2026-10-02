package br.com.voluntplus.reviews.adapter.in.web;

import br.com.voluntplus.reviews.application.exception.OwnServiceReviewNotAllowedException;
import br.com.voluntplus.reviews.application.exception.ReviewableServiceNotFoundException;
import br.com.voluntplus.reviews.domain.exception.InvalidReviewException;
import br.com.voluntplus.users.api.CurrentUserUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = ReviewController.class)
public class ReviewExceptionHandler {

	@ExceptionHandler(InvalidReviewRequestException.class)
	public ResponseEntity<ProblemDetail> handleInvalidRequest(InvalidReviewRequestException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Invalid review request", exception.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ProblemDetail> handleUnreadableRequest() {
		return problem(
				HttpStatus.BAD_REQUEST,
				"Invalid review request",
				"The request body is malformed or contains an invalid value");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ProblemDetail> handleInvalidPathValue() {
		return problem(HttpStatus.BAD_REQUEST, "Invalid request", "The path contains an invalid value");
	}

	@ExceptionHandler(CurrentUserUnavailableException.class)
	public ResponseEntity<ProblemDetail> handleUnavailableUser(CurrentUserUnavailableException exception) {
		return problem(HttpStatus.UNAUTHORIZED, "Current user unavailable", exception.getMessage());
	}

	@ExceptionHandler(OwnServiceReviewNotAllowedException.class)
	public ResponseEntity<ProblemDetail> handleOwnService(OwnServiceReviewNotAllowedException exception) {
		return problem(HttpStatus.FORBIDDEN, "Review not allowed", exception.getMessage());
	}

	@ExceptionHandler(ReviewableServiceNotFoundException.class)
	public ResponseEntity<ProblemDetail> handleServiceNotFound(ReviewableServiceNotFoundException exception) {
		return problem(HttpStatus.NOT_FOUND, "Volunteer service not found", exception.getMessage());
	}

	@ExceptionHandler(InvalidReviewException.class)
	public ResponseEntity<ProblemDetail> handleInvalidReview(InvalidReviewException exception) {
		return problem(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid review", exception.getMessage());
	}

	private ResponseEntity<ProblemDetail> problem(HttpStatus status, String title, String detail) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
		problem.setTitle(title);
		return ResponseEntity.status(status).body(problem);
	}
}
