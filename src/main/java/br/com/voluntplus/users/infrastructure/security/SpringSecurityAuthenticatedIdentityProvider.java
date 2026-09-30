package br.com.voluntplus.users.infrastructure.security;

import br.com.voluntplus.users.application.exception.AuthenticatedIdentityUnavailableException;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentity;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuthenticatedIdentityProvider implements AuthenticatedIdentityProvider {

	@Override
	public String currentClerkUserId() {
		JwtAuthenticationToken jwtAuthentication = requireAuthenticatedJwt();
		return requireSubject(jwtAuthentication);
	}

	@Override
	public AuthenticatedIdentity currentIdentity() {
		JwtAuthenticationToken jwtAuthentication = requireAuthenticatedJwt();
		String subject = requireSubject(jwtAuthentication);

		String email = jwtAuthentication.getToken().getClaimAsString("primaryEmail");
		if (email == null || email.isBlank()) {
			throw new AuthenticatedIdentityUnavailableException(
					"The authenticated identity has no primaryEmail claim");
		}

		if (email.length() > 320) {
			throw new AuthenticatedIdentityUnavailableException(
					"The authenticated identity contains data longer than supported");
		}

		return new AuthenticatedIdentity(subject, email);
	}

	private JwtAuthenticationToken requireAuthenticatedJwt() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)
				|| !authentication.isAuthenticated()) {
			throw new AuthenticatedIdentityUnavailableException("No authenticated identity is available");
		}
		return jwtAuthentication;
	}

	private String requireSubject(JwtAuthenticationToken jwtAuthentication) {
		String subject = jwtAuthentication.getToken().getSubject();
		if (subject == null || subject.isBlank()) {
			throw new AuthenticatedIdentityUnavailableException("The authenticated identity has no subject");
		}

		if (subject.length() > 255) {
			throw new AuthenticatedIdentityUnavailableException(
					"The authenticated identity contains data longer than supported");
		}
		return subject;
	}
}
