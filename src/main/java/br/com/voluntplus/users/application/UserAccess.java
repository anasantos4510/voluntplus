package br.com.voluntplus.users.application;

import br.com.voluntplus.users.UserAccount;

/** Public application interface used by the other business modules. */
public interface UserAccess {
    UserAccount requireRole(String role);
    java.util.Map<String, Object> providerSummary(Long id);
    String displayName(Long id);
}
