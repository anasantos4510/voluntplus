package br.com.voluntplus.volunteerservices.adapter.in.web;

import br.com.voluntplus.volunteerservices.application.port.in.ChangeVolunteerServiceStatusUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.CreateVolunteerServiceUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.CreateVolunteerServiceUseCase.CreateVolunteerServiceCommand;
import br.com.voluntplus.volunteerservices.application.port.in.CreateVolunteerServiceUseCase.CreateVolunteerServiceResult;
import br.com.voluntplus.volunteerservices.application.port.in.DeleteVolunteerServiceUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.EditVolunteerServiceUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.GetPublicVolunteerServiceUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.ListOwnedVolunteerServicesUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.ListPublicVolunteerServicesUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.OwnedVolunteerServiceResult;
import br.com.voluntplus.volunteerservices.application.port.in.PublicVolunteerServiceResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
public class VolunteerServiceController {

	private final CreateVolunteerServiceUseCase createVolunteerService;
	private final ListPublicVolunteerServicesUseCase listPublicVolunteerServices;
	private final GetPublicVolunteerServiceUseCase getPublicVolunteerService;
	private final ListOwnedVolunteerServicesUseCase listOwnedVolunteerServices;
	private final EditVolunteerServiceUseCase editVolunteerService;
	private final ChangeVolunteerServiceStatusUseCase changeVolunteerServiceStatus;
	private final DeleteVolunteerServiceUseCase deleteVolunteerService;

	public VolunteerServiceController(
			CreateVolunteerServiceUseCase createVolunteerService,
			ListPublicVolunteerServicesUseCase listPublicVolunteerServices,
			GetPublicVolunteerServiceUseCase getPublicVolunteerService,
			ListOwnedVolunteerServicesUseCase listOwnedVolunteerServices,
			EditVolunteerServiceUseCase editVolunteerService,
			ChangeVolunteerServiceStatusUseCase changeVolunteerServiceStatus,
			DeleteVolunteerServiceUseCase deleteVolunteerService) {
		this.createVolunteerService = createVolunteerService;
		this.listPublicVolunteerServices = listPublicVolunteerServices;
		this.getPublicVolunteerService = getPublicVolunteerService;
		this.listOwnedVolunteerServices = listOwnedVolunteerServices;
		this.editVolunteerService = editVolunteerService;
		this.changeVolunteerServiceStatus = changeVolunteerServiceStatus;
		this.deleteVolunteerService = deleteVolunteerService;
	}

	@GetMapping
	@Operation(summary = "Consultar o catálogo público de serviços voluntários")
	@ApiResponse(responseCode = "200", description = "Catálogo consultado")
	public List<VolunteerServiceResponse> listPublicServices() {
		return listPublicVolunteerServices.listPublicServices().stream()
				.map(this::toResponse)
				.toList();
	}

	@GetMapping("/mine")
	@Operation(
			summary = "Listar os próprios serviços para gerenciamento",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Serviços do ofertante consultados"),
			@ApiResponse(responseCode = "401", description = "Identidade ou perfil indisponível"),
			@ApiResponse(responseCode = "403", description = "Papel atual não autorizado")
	})
	public List<VolunteerServiceResponse> listOwnedServices() {
		return listOwnedVolunteerServices.listOwnedServices().stream()
				.map(this::toResponse)
				.toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consultar os detalhes públicos de um serviço voluntário")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Serviço encontrado"),
			@ApiResponse(responseCode = "404", description = "Serviço não encontrado")
	})
	public VolunteerServiceResponse getPublicService(@PathVariable UUID id) {
		return toResponse(getPublicVolunteerService.getPublicService(id));
	}

	@PostMapping
	@Operation(
			summary = "Cadastrar um serviço voluntário",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Serviço cadastrado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido"),
			@ApiResponse(responseCode = "401", description = "Identidade ou perfil indisponível"),
			@ApiResponse(responseCode = "403", description = "Papel atual não autorizado"),
			@ApiResponse(responseCode = "422", description = "Regra de domínio não atendida")
	})
	public ResponseEntity<VolunteerServiceResponse> create(
			@RequestBody CreateVolunteerServiceRequest request) {
		CreateVolunteerServiceCommand command = CreateVolunteerServiceRequestMapper.toCommand(request);
		CreateVolunteerServiceResult result = createVolunteerService.create(command);
		VolunteerServiceResponse response = VolunteerServiceResponseMapper.toResponse(
				result.service(),
				result.owner());
		URI location = URI.create("/api/services/" + result.service().getId());

		return ResponseEntity.status(HttpStatus.CREATED)
				.location(location)
				.body(response);
	}

	@PatchMapping("/{id}")
	@Operation(
			summary = "Editar os dados ou alterar o status de um serviço voluntário",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Serviço atualizado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido"),
			@ApiResponse(responseCode = "401", description = "Identidade ou perfil indisponível"),
			@ApiResponse(responseCode = "403", description = "Papel atual não autorizado"),
			@ApiResponse(responseCode = "404", description = "Serviço não encontrado"),
			@ApiResponse(responseCode = "409", description = "Estado do serviço incompatível"),
			@ApiResponse(responseCode = "422", description = "Regra de domínio não atendida")
	})
	public VolunteerServiceResponse update(
			@PathVariable UUID id,
			@RequestBody UpdateVolunteerServiceRequest request) {
		OwnedVolunteerServiceResult result;
		if (UpdateVolunteerServiceRequestMapper.isStatusChange(request)) {
			result = changeVolunteerServiceStatus.changeStatus(
					id,
					UpdateVolunteerServiceRequestMapper.toStatus(request));
		} else {
			result = editVolunteerService.edit(
					id,
					UpdateVolunteerServiceRequestMapper.toEditCommand(request));
		}
		return toResponse(result);
	}

	@DeleteMapping("/{id}")
	@Operation(
			summary = "Excluir logicamente um serviço voluntário",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Serviço excluído"),
			@ApiResponse(responseCode = "401", description = "Identidade ou perfil indisponível"),
			@ApiResponse(responseCode = "403", description = "Papel atual não autorizado"),
			@ApiResponse(responseCode = "404", description = "Serviço não encontrado")
	})
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		deleteVolunteerService.delete(id);
		return ResponseEntity.noContent().build();
	}

	private VolunteerServiceResponse toResponse(PublicVolunteerServiceResult result) {
		return VolunteerServiceResponseMapper.toResponse(
				result.service(),
				result.owner(),
				result.reviewStatistics());
	}

	private VolunteerServiceResponse toResponse(OwnedVolunteerServiceResult result) {
		return VolunteerServiceResponseMapper.toResponse(result.service(), result.owner());
	}
}
