package br.com.voluntplus.users.infrastructure;

import br.com.voluntplus.users.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByClerkUserId(String clerkUserId);
}
