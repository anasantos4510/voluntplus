package br.com.voluntplus.users.application.port.out;

import br.com.voluntplus.users.UserAccount;
import java.util.Optional;

public interface UserAccountStore {
    Optional<UserAccount> findById(Long id);
    Optional<UserAccount> findByClerkUserId(String clerkUserId);
    UserAccount save(UserAccount account);
}
