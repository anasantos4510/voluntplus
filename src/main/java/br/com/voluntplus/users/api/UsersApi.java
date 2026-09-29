package br.com.voluntplus.users.api;

import java.util.Optional;
import java.util.UUID;

public interface UsersApi {

	Optional<UserSummary> findById(UUID userId);

	UserSummary getCurrentUser();

	UserSummary requireCurrentUserWithRole(CurrentRole requiredRole);
}
