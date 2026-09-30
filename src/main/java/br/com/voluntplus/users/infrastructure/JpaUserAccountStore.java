package br.com.voluntplus.users.infrastructure;

import br.com.voluntplus.users.UserAccount;
import br.com.voluntplus.users.application.port.out.UserAccountStore;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class JpaUserAccountStore implements UserAccountStore {
    private final UserAccountRepository repository;
    public JpaUserAccountStore(UserAccountRepository repository) { this.repository = repository; }
    public Optional<UserAccount> findById(Long id) { return repository.findById(id); }
    public Optional<UserAccount> findByClerkUserId(String id) { return repository.findByClerkUserId(id); }
    public UserAccount save(UserAccount account) { return repository.save(account); }
}
