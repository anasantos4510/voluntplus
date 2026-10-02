package br.com.voluntplus.volunteerservices.adapter.in.web;

import br.com.voluntplus.users.api.CurrentUserUnavailableException;
import br.com.voluntplus.volunteerservices.application.exception.ManagedVolunteerServiceNotFoundException;
import br.com.voluntplus.volunteerservices.application.exception.PublicVolunteerServiceNotFoundException;
import br.com.voluntplus.volunteerservices.domain.exception.InvalidServiceImageException;
import br.com.voluntplus.volunteerservices.domain.exception.InvalidVolunteerServiceException;
import br.com.voluntplus.volunteerservices.domain.exception.ServiceStatusAlreadySetException;
import br.com.voluntplus.volunteerservices.domain.exception.VolunteerServiceStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = VolunteerServiceController.class)
public class VolunteerServiceExceptionHandler {

	@ExceptionHandler(InvalidVolunteerServiceRequestException.class)
	public ResponseEntity<ProblemDetail> handleInvalidRequest(InvalidVolunteerServiceRequestException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Invalid volunteer service request", exception.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ProblemDetail> handleUnreadableRequest() {
		return problem(
				HttpStatus.BAD_REQUEST,
				"Invalid volunteer service request",
				"The request body is malformed or contains an invalid value");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ProblemDetail> handleInvalidPathValue() {
		return problem(HttpStatus.BAD_REQUEST, "Invalid request", "The path contains an invalid value");
	}

	@ExceptionHandler(PublicVolunteerServiceNotFoundException.class)
	public ResponseEntity<ProblemDetail> handlePublicServiceNotFound(
			PublicVolunteerServiceNotFoundException exception) {
		return problem(HttpStatus.NOT_FOUND, "Volunteer service not found", exception.getMessage());
	}

	@ExceptionHandler(ManagedVolunteerServiceNotFoundException.class)
	public ResponseEntity<ProblemDetail> handleManagedServiceNotFound(
			ManagedVolunteerServiceNotFoundException exception) {
		return problem(HttpStatus.NOT_FOUND, "Volunteer service not found", exception.getMessage());
	}

	@ExceptionHandler(InvalidServiceImageException.class)
	public ResponseEntity<ProblemDetail> handleInvalidImage(InvalidServiceImageException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Invalid service image", exception.getMessage());
	}

	@ExceptionHandler(InvalidVolunteerServiceException.class)
	public ResponseEntity<ProblemDetail> handleInvalidService(InvalidVolunteerServiceException exception) {
		return problem(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid volunteer service", exception.getMessage());
	}

	@ExceptionHandler(CurrentUserUnavailableException.class)
	public ResponseEntity<ProblemDetail> handleUnavailableUser(CurrentUserUnavailableException exception) {
		return problem(HttpStatus.UNAUTHORIZED, "Current user unavailable", exception.getMessage());
	}

	@ExceptionHandler({ServiceStatusAlreadySetException.class, VolunteerServiceStateException.class})
	public ResponseEntity<ProblemDetail> handleStateConflict(RuntimeException exception) {
		return problem(HttpStatus.CONFLICT, "Volunteer service state conflict", exception.getMessage());
	}

	private ResponseEntity<ProblemDetail> problem(HttpStatus status, String title, String detail) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
		problem.setTitle(title);
		return ResponseEntity.status(status).body(problem);
	}
}
