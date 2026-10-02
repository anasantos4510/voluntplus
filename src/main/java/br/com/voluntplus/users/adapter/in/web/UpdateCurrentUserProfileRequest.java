package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.domain.model.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Dados editaveis do perfil do usuario autenticado")
public record UpdateCurrentUserProfileRequest(
		@Size(max = 255)
		String fullName,

		@Schema(type = "string", format = "date", example = "1995-05-20")
		LocalDate birthDate,

		Gender gender,

		@Size(max = 255)
		String organizationName,

		@Pattern(regexp = "\\d{14}")
		@Schema(example = "12345678000195", nullable = true)
		String cnpj) {
}
