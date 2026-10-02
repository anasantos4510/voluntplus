package br.com.voluntplus.reviews.adapter.in.web;

import br.com.voluntplus.reviews.application.port.in.ListServiceReviewsUseCase;
import br.com.voluntplus.reviews.application.port.in.RegisterReviewUseCase;
import br.com.voluntplus.reviews.application.port.in.ReviewResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services/{serviceId}/reviews")
public class ReviewController {

	private final RegisterReviewUseCase registerReview;
	private final ListServiceReviewsUseCase listServiceReviews;

	public ReviewController(
			RegisterReviewUseCase registerReview,
			ListServiceReviewsUseCase listServiceReviews) {
		this.registerReview = registerReview;
		this.listServiceReviews = listServiceReviews;
	}

	@PostMapping
	@Operation(
			summary = "Registrar uma avaliação de serviço",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Avaliação registrada"),
		@ApiResponse(responseCode = "400", description = "Requisição inválida"),
		@ApiResponse(responseCode = "401", description = "Identidade ou perfil indisponível"),
		@ApiResponse(responseCode = "403", description = "Usuário não autorizado a avaliar"),
		@ApiResponse(responseCode = "404", description = "Serviço não encontrado"),
		@ApiResponse(responseCode = "422", description = "Regra da avaliação não atendida")
	})
	public ResponseEntity<ReviewResponse> register(
			@PathVariable UUID serviceId,
			@RequestBody CreateReviewRequest request) {
		ReviewResult result = registerReview.register(ReviewWebMapper.toCommand(serviceId, request));
		ReviewResponse response = ReviewWebMapper.toResponse(result);
		URI location = URI.create("/api/services/" + serviceId + "/reviews/" + response.id());
		return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
	}

	@GetMapping
	@Operation(summary = "Consultar as avaliações de um serviço")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Avaliações consultadas"),
		@ApiResponse(responseCode = "400", description = "Identificador inválido"),
		@ApiResponse(responseCode = "404", description = "Serviço não encontrado")
	})
	public List<ReviewResponse> list(@PathVariable UUID serviceId) {
		return listServiceReviews.listByServiceId(serviceId).stream()
				.map(ReviewWebMapper::toResponse)
				.toList();
	}
}
