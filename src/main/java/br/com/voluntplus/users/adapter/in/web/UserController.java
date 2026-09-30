package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.UserAccount;
import br.com.voluntplus.users.application.UserAccounts;
import br.com.voluntplus.users.application.UserAccounts.IndividualRegistration;
import br.com.voluntplus.users.application.UserAccounts.OrganizationRegistration;
import br.com.voluntplus.users.application.UserAccounts.RoleChange;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserAccounts accounts;
    public UserController(UserAccounts accounts) { this.accounts = accounts; }

    @GetMapping("/me")
    public UserAccount me() { return accounts.me(); }
    @GetMapping("/{id}")
    public Map<String, Object> publicProfile(@PathVariable Long id) { return accounts.publicProfile(id); }
    @PostMapping("/individuals")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccount registerIndividual(@Valid @RequestBody IndividualRegistration input) {
        return accounts.registerIndividual(input);
    }
    @PostMapping("/organizations")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccount registerOrganization(@Valid @RequestBody OrganizationRegistration input) {
        return accounts.registerOrganization(input);
    }
    @PatchMapping("/me/role")
    public UserAccount changeRole(@Valid @RequestBody RoleChange input) { return accounts.changeRole(input); }
    @PatchMapping("/me")
    public UserAccount update(@RequestBody Map<String, Object> input) { return accounts.update(input); }
}
