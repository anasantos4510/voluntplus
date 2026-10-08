package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.application.exception.AuthenticatedIdentityUnavailableException;
import br.com.voluntplus.users.application.exception.UserAlreadyRegisteredException;
import br.com.voluntplus.users.application.exception.UserProfileNotFoundException;
import br.com.voluntplus.users.domain.exception.InvalidUserRegistrationException;
import br.com.voluntplus.users.domain.exception.InvalidUserProfileUpdateException;
import br.com.voluntplus.users.domain.exception.UserRoleAlreadyActiveException;
import br.com.voluntplus.users.domain.exception.UserRoleChangeNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {

	@ExceptionHandler(InvalidUserRegistrationException.class)
	public ResponseEntity<ProblemDetail> handleInvalidRegistration(InvalidUserRegistrationException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Invalid user registration", exception.getMessage());
	}

	@ExceptionHandler(InvalidUserProfileUpdateException.class)
	public ResponseEntity<ProblemDetail> handleInvalidProfileUpdate(InvalidUserProfileUpdateException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Invalid user profile update", exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleInvalidRequest(MethodArgumentNotValidException exception) {
		String detail = exception.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(error -> error.getField() + " " + error.getDefaultMessage())
				.orElse("The request contains invalid data");
		return problem(HttpStatus.BAD_REQUEST, "Invalid request", detail);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ProblemDetail> handleUnreadableRequest() {
		return problem(HttpStatus.BAD_REQUEST, "Invalid request", "The request body is malformed or contains an invalid value");
	}

	@ExceptionHandler(AuthenticatedIdentityUnavailableException.class)
	public ResponseEntity<ProblemDetail> handleUnavailableIdentity(AuthenticatedIdentityUnavailableException exception) {
		return problem(HttpStatus.UNAUTHORIZED, "Authenticated identity unavailable", exception.getMessage());
	}

	@ExceptionHandler(UserAlreadyRegisteredException.class)
	public ResponseEntity<ProblemDetail> handleAlreadyRegistered(UserAlreadyRegisteredException exception) {
		return problem(HttpStatus.CONFLICT, "User already registered", exception.getMessage());
	}

	@ExceptionHandler(UserProfileNotFoundException.class)
	public ResponseEntity<ProblemDetail> handleProfileNotFound(UserProfileNotFoundException exception) {
		return problem(HttpStatus.NOT_FOUND, "User profile not found", exception.getMessage());
	}

	@ExceptionHandler(UserRoleAlreadyActiveException.class)
	public ResponseEntity<ProblemDetail> handleRoleAlreadyActive(UserRoleAlreadyActiveException exception) {
		return problem(HttpStatus.CONFLICT, "User role already active", exception.getMessage());
	}

	@ExceptionHandler(UserRoleChangeNotAllowedException.class)
	public ResponseEntity<ProblemDetail> handleRoleChangeNotAllowed(UserRoleChangeNotAllowedException exception) {
		return problem(HttpStatus.UNPROCESSABLE_ENTITY, "User role change not allowed", exception.getMessage());
	}

	private ResponseEntity<ProblemDetail> problem(HttpStatus status, String title, String detail) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
		problem.setTitle(title);
		return ResponseEntity.status(status).body(problem);
	}
}
