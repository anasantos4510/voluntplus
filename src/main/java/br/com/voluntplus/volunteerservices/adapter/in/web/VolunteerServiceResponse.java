package br.com.voluntplus.volunteerservices.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record VolunteerServiceResponse(
		UUID id,
		UUID ownerId,
		String name,
		String descricao,
		String modalities,
		String idCategoria,
		CategoryResponse categoria,
		String status,
		double avaliacaoMedia,
		long quantidadeAvaliacoes,
		@Schema(
				description = "Data URL opcional da imagem do serviço",
				nullable = true)
		String providerImage,
		Instant publicationDate,
		Instant updatedAt,
		String cep,
		String estado,
		String cidade,
		String bairro,
		Integer tipoLocalizacao,
		LocationResponse localizacao,
		List<String> diaDaSemana,
		List<String> turno,
		List<ScheduleResponse> agendamentos,
		String whatsapp,
		String telefone,
		String instagram,
		String site,
		ContactResponse contato,
		UserResponse usuario) {

	public record CategoryResponse(String id, String nome) {
	}

	public record LocationResponse(
			String cep,
			String estado,
			String cidade,
			String bairro,
			Integer tipoLocalizacao) {
	}

	public record ScheduleResponse(String diaSemana, String turno) {
	}

	public record ContactResponse(String telefone, String instagram, String site) {
	}

	public record UserResponse(
			UUID id,
			String fullName,
			String organizationName,
			String email,
			String tipoUsuario,
			String genero,
			Integer idade) {
	}
}
