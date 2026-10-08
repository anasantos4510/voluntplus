package br.com.voluntplus.users.api;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UsersApi {

	Optional<UserSummary> findById(UUID userId);

	Map<UUID, UserSummary> findAllByIds(Set<UUID> userIds);

	UserSummary getCurrentUser();

	UserSummary requireCurrentUserWithRole(CurrentRole requiredRole);
}
