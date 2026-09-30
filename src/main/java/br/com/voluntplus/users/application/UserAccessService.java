package br.com.voluntplus.users.application;

import br.com.voluntplus.users.UserAccount;
import br.com.voluntplus.users.application.UserAccess;
import br.com.voluntplus.users.application.port.out.UserAccountStore;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserAccessService implements UserAccess {
    private final UserAccountStore users;
    private final AuthenticatedIdentityProvider identity;

    public UserAccessService(UserAccountStore users, AuthenticatedIdentityProvider identity) {
        this.users = users;
        this.identity = identity;
    }

    @Override
    public String displayName(Long id) {
        return users.findById(id).map(user -> "ORGANIZATION".equals(user.personType)
                ? user.organizationName : user.fullName).orElse("Usuário");
    }

    @Override
    public java.util.Map<String, Object> providerSummary(Long id) {
        return users.findById(id).map(user -> {
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", user.id);
            result.put("personType", user.personType);
            result.put("currentRole", user.currentRole);
            result.put("organizationName", user.organizationName);
            result.put("fullName", user.fullName);
            if ("INDIVIDUAL".equals(user.personType)) {
                result.put("gender", user.gender);
                if (user.birthDate != null) result.put("idade", java.time.Period.between(user.birthDate, java.time.LocalDate.now()).getYears());
            }
            return result;
        }).orElseGet(java.util.HashMap::new);
    }

    @Override
    public UserAccount requireRole(String role) {
        UserAccount account = users.findByClerkUserId(identity.currentIdentity())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Perfil não cadastrado"));
        if (!role.equals(account.currentRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Papel sem permissão");
        }
        return account;
    }
}
