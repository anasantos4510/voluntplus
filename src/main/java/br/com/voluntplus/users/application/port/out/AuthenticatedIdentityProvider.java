package br.com.voluntplus.users.application.port.out;

public interface AuthenticatedIdentityProvider {

	String currentClerkUserId();

	AuthenticatedIdentity currentIdentity();
}
