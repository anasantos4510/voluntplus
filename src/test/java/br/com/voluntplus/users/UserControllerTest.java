package br.com.voluntplus.users;

import br.com.voluntplus.users.application.port.out.AuthenticatedIdentityProvider;
import br.com.voluntplus.users.application.port.out.UserAccountStore;
import br.com.voluntplus.users.application.UserAccounts;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private final UserAccountStore users = mock(UserAccountStore.class);
    private final AuthenticatedIdentityProvider identity = mock(AuthenticatedIdentityProvider.class);
    private final UserAccounts accounts = new UserAccounts(users, identity);

    @Test void beneficiaryProfileIsNotPublicById() {
        UserAccount beneficiary = new UserAccount();
        beneficiary.id = 2L;
        beneficiary.currentRole = "BENEFICIARY";
        when(users.findById(2L)).thenReturn(Optional.of(beneficiary));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> accounts.publicProfile(2L));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test void organizationCannotSwitchToBeneficiary() {
        UserAccount offerer = new UserAccount();
        offerer.personType = "ORGANIZATION";
        offerer.currentRole = "OFFERER";
        when(identity.currentIdentity()).thenReturn("clerk-user");
        when(users.findByClerkUserId("clerk-user")).thenReturn(Optional.of(offerer));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> accounts.changeRole(new UserAccounts.RoleChange("BENEFICIARY")));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(users, never()).save(any());
    }

    @Test void individualCanSwitchRoleWithoutLosingAccount() {
        UserAccount individual = new UserAccount();
        individual.personType = "INDIVIDUAL";
        individual.currentRole = "OFFERER";
        when(identity.currentIdentity()).thenReturn("clerk-user");
        when(users.findByClerkUserId("clerk-user")).thenReturn(Optional.of(individual));
        when(users.save(individual)).thenReturn(individual);

        UserAccount updated = accounts.changeRole(new UserAccounts.RoleChange("BENEFICIARY"));
        assertSame(individual, updated);
        assertEquals("BENEFICIARY", updated.currentRole);
        verify(users).save(individual);
    }
}
