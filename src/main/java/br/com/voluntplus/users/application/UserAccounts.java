package br.com.voluntplus.users.application;

import br.com.voluntplus.users.UserAccount;
import br.com.voluntplus.users.application.port.out.UserAccountStore;
import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.Map;

@Service
public class UserAccounts {
    private final UserAccountStore users;
    private final AuthenticatedIdentityProvider identity;
    public UserAccounts(UserAccountStore users, AuthenticatedIdentityProvider identity) {
        this.users = users;
        this.identity = identity;
    }

    public record IndividualRegistration(@NotBlank String fullName, @NotNull LocalDate birthDate,
                                         @NotBlank String gender, @NotBlank String initialRole) {}
    public record OrganizationRegistration(@NotBlank String organizationName, String cnpj) {}
    public record RoleChange(@NotBlank String role) {}

    private UserAccount current() {
        return users.findByClerkUserId(identity.currentIdentity())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));
    }

    private void ensureNew() {
        if (users.findByClerkUserId(identity.currentIdentity()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Perfil já cadastrado");
        }
    }

    public UserAccount me() { return current(); }

    public Map<String, Object> publicProfile(Long id) {
        UserAccount user = users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));
        if (!"OFFERER".equals(user.currentRole)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado");
        }
        Map<String, Object> profile = new java.util.HashMap<>();
        profile.put("id", user.id);
        profile.put("personType", user.personType);
        profile.put("currentRole", user.currentRole);
        profile.put("fullName", user.fullName);
        profile.put("organizationName", user.organizationName);
        profile.put("cnpj", user.cnpj);
        profile.put("gender", user.gender);
        if (user.birthDate != null) profile.put("idade", java.time.Period.between(user.birthDate, LocalDate.now()).getYears());
        return profile;
    }

    @Transactional
    public UserAccount registerIndividual(IndividualRegistration input) {
        ensureNew();
        if (!input.initialRole().equals("OFFERER") && !input.initialRole().equals("BENEFICIARY")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Papel inválido");
        }
        UserAccount user = new UserAccount();
        user.clerkUserId = identity.currentIdentity();
        user.personType = "INDIVIDUAL";
        user.currentRole = input.initialRole();
        user.fullName = input.fullName().trim();
        user.birthDate = input.birthDate();
        user.gender = input.gender();
        return users.save(user);
    }

    @Transactional
    public UserAccount registerOrganization(OrganizationRegistration input) {
        ensureNew();
        UserAccount user = new UserAccount();
        user.clerkUserId = identity.currentIdentity();
        user.personType = "ORGANIZATION";
        user.currentRole = "OFFERER";
        user.organizationName = input.organizationName().trim();
        user.cnpj = input.cnpj();
        return users.save(user);
    }

    @Transactional
    public UserAccount changeRole(RoleChange input) {
        UserAccount user = current();
        if (user.personType.equals("ORGANIZATION") ||
                (!input.role().equals("OFFERER") && !input.role().equals("BENEFICIARY"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Papel inválido para esta conta");
        }
        user.currentRole = input.role();
        return users.save(user);
    }

    @Transactional
    public UserAccount update(Map<String, Object> changes) {
        UserAccount user = current();
        if (user.personType.equals("ORGANIZATION")) {
            user.organizationName = value(changes, "organizationName", user.organizationName);
            user.cnpj = value(changes, "cnpj", user.cnpj);
        } else {
            user.fullName = value(changes, "fullName", user.fullName);
            user.gender = value(changes, "gender", user.gender);
            if (changes.containsKey("birthDate")) {
                String date = value(changes, "birthDate", null);
                user.birthDate = date == null || date.isBlank() ? null : LocalDate.parse(date);
            }
        }
        return users.save(user);
    }

    private static String value(Map<String, Object> changes, String field, String previous) {
        Object value = changes.get(field);
        return value == null ? changes.containsKey(field) ? null : previous : value.toString();
    }
}
