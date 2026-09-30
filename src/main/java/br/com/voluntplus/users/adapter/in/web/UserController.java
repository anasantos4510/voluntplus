package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.application.port.in.CompleteIndividualRegistrationUseCase;
import br.com.voluntplus.users.application.port.in.CompleteIndividualRegistrationUseCase.CompleteIndividualRegistrationCommand;
import br.com.voluntplus.users.application.port.in.CompleteIndividualRegistrationUseCase.IndividualRegistrationResult;
import br.com.voluntplus.users.application.port.in.ChangeCurrentUserRoleUseCase;
import br.com.voluntplus.users.application.port.in.ChangeCurrentUserRoleUseCase.ChangeCurrentUserRoleCommand;
import br.com.voluntplus.users.application.port.in.ChangeCurrentUserRoleUseCase.ChangeCurrentUserRoleResult;
import br.com.voluntplus.users.application.port.in.CompleteOrganizationRegistrationUseCase;
import br.com.voluntplus.users.application.port.in.CompleteOrganizationRegistrationUseCase.CompleteOrganizationRegistrationCommand;
import br.com.voluntplus.users.application.port.in.CompleteOrganizationRegistrationUseCase.OrganizationRegistrationResult;
import br.com.voluntplus.users.application.port.in.GetCurrentUserProfileUseCase;
import br.com.voluntplus.users.application.port.in.GetCurrentUserProfileUseCase.CurrentUserProfileResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users")
public class UserController {

	private final CompleteIndividualRegistrationUseCase completeIndividualRegistration;
	private final CompleteOrganizationRegistrationUseCase completeOrganizationRegistration;
	private final GetCurrentUserProfileUseCase getCurrentUserProfile;
	private final ChangeCurrentUserRoleUseCase changeCurrentUserRole;

	public UserController(
			CompleteIndividualRegistrationUseCase completeIndividualRegistration,
			CompleteOrganizationRegistrationUseCase completeOrganizationRegistration,
			GetCurrentUserProfileUseCase getCurrentUserProfile,
			ChangeCurrentUserRoleUseCase changeCurrentUserRole) {
		this.completeIndividualRegistration = completeIndividualRegistration;
		this.completeOrganizationRegistration = completeOrganizationRegistration;
		this.getCurrentUserProfile = getCurrentUserProfile;
		this.changeCurrentUserRole = changeCurrentUserRole;
	}

	@PostMapping("/individuals")
	@Operation(
			summary = "Criar perfil de pessoa física no Volunt+",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Cadastro concluído"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "401", description = "Identidade não autenticada ou incompleta",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "409", description = "Identidade já cadastrada",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	})
	public ResponseEntity<IndividualRegistrationResponse> completeIndividualRegistration(
			@Valid @RequestBody IndividualRegistrationRequest request) {
		IndividualRegistrationResult result = completeIndividualRegistration.complete(
				new CompleteIndividualRegistrationCommand(
						request.fullName(),
						request.birthDate(),
						request.gender(),
						request.initialRole()));

		IndividualRegistrationResponse response = new IndividualRegistrationResponse(
				result.id(),
				result.personType(),
				result.currentRole(),
				result.fullName(),
				result.birthDate(),
				result.gender(),
				result.email());

		URI location = URI.create("/api/v1/users/" + result.id());
		return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
	}

	@PostMapping("/organizations")
	@Operation(
			summary = "Criar perfil de pessoa jurídica no Volunt+",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Cadastro concluído"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "401", description = "Identidade não autenticada ou incompleta",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "409", description = "Identidade já cadastrada",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	})
	public ResponseEntity<OrganizationRegistrationResponse> completeOrganizationRegistration(
			@Valid @RequestBody OrganizationRegistrationRequest request) {
		OrganizationRegistrationResult result = completeOrganizationRegistration.complete(
				new CompleteOrganizationRegistrationCommand(
						request.organizationName(),
						request.cnpj()));

		OrganizationRegistrationResponse response = new OrganizationRegistrationResponse(
				result.id(),
				result.personType(),
				result.currentRole(),
				result.organizationName(),
				result.cnpj(),
				result.email());

		URI location = URI.create("/api/v1/users/" + result.id());
		return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
	}

	@GetMapping("/me")
	@Operation(
			summary = "Consultar o próprio perfil",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Perfil encontrado"),
			@ApiResponse(responseCode = "401", description = "Identidade não autenticada ou incompleta",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "404", description = "Perfil não encontrado",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	})
	public ResponseEntity<UserProfileResponse> getCurrentProfile() {
		CurrentUserProfileResult result = getCurrentUserProfile.getCurrentProfile();
		UserProfileResponse response = new UserProfileResponse(
				result.id(),
				result.personType(),
				result.fullName(),
				result.organizationName(),
				result.email(),
				result.birthDate(),
				result.gender(),
				result.currentRole());

		return ResponseEntity.ok()
				.cacheControl(CacheControl.noStore())
				.body(response);
	}

	@PatchMapping("/me/role")
	@Operation(
			summary = "Alternar o papel da pessoa física",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Papel alterado"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "401", description = "Identidade não autenticada ou incompleta",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "404", description = "Perfil do usuário não encontrado",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "409", description = "Papel solicitado já está ativo",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "422", description = "Tipo de pessoa não pode alternar papel",
					content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
	})
	public ResponseEntity<ChangeCurrentUserRoleResponse> changeCurrentRole(
			@Valid @RequestBody ChangeCurrentUserRoleRequest request) {
		ChangeCurrentUserRoleResult result = changeCurrentUserRole.change(
				new ChangeCurrentUserRoleCommand(request.role()));
		ChangeCurrentUserRoleResponse response = new ChangeCurrentUserRoleResponse(
				result.userId(),
				result.personType(),
				result.currentRole());

		return ResponseEntity.ok(response);
	}
}
