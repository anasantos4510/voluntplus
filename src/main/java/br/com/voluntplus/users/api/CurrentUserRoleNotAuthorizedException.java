package br.com.voluntplus.users.api;

public class CurrentUserRoleNotAuthorizedException extends RuntimeException {

	private final CurrentRole currentRole;
	private final CurrentRole requiredRole;

	public CurrentUserRoleNotAuthorizedException(CurrentRole currentRole, CurrentRole requiredRole) {
		super("The current user role " + currentRole + " is not authorized; required role: " + requiredRole);
		this.currentRole = currentRole;
		this.requiredRole = requiredRole;
	}

	public CurrentRole getCurrentRole() {
		return currentRole;
	}

	public CurrentRole getRequiredRole() {
		return requiredRole;
	}
}
