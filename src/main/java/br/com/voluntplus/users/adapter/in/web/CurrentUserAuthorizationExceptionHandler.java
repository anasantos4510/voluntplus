package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.api.CurrentUserRoleNotAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CurrentUserAuthorizationExceptionHandler {

	@ExceptionHandler(CurrentUserRoleNotAuthorizedException.class)
	public ResponseEntity<ProblemDetail> handleRoleNotAuthorized(
			CurrentUserRoleNotAuthorizedException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
		problem.setTitle("Current user role is not authorized");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
	}
}
