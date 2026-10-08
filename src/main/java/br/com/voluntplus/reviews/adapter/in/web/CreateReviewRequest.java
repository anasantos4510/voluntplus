package br.com.voluntplus.reviews.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para registrar uma avaliação de serviço")
public record CreateReviewRequest(
		@Schema(description = "Nota inteira entre 1 e 5", example = "5") Integer nota,
		@Schema(description = "Comentário opcional com até 500 caracteres", nullable = true)
		String comentario) {
}
