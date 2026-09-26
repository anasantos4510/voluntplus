package br.com.voluntplus.users.infrastructure.security;

import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuthenticatedIdentityProvider implements AuthenticatedIdentityProvider {

	@Override
	public String currentIdentity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)
				|| !authentication.isAuthenticated()) {
			throw new IllegalStateException("No authenticated identity is available");
		}

		String subject = jwtAuthentication.getToken().getSubject();
		if (subject == null || subject.isBlank()) {
			throw new IllegalStateException("The authenticated identity has no subject");
		}

		return subject;
	}
}
