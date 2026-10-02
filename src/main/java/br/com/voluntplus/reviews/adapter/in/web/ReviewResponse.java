package br.com.voluntplus.reviews.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Avaliação de um serviço voluntário")
public record ReviewResponse(
		UUID id,
		UUID serviceId,
		UUID authorId,
		String nomeAutor,
		int nota,
		String comentario,
		Instant dataCriacao) {
}
