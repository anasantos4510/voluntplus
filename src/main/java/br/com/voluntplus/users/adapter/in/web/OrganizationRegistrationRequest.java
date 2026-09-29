package br.com.voluntplus.users.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties({"role", "currentRole", "initialRole", "email", "clerkUserId"})
@Schema(description = "Dados da organização usados para criar o perfil no Volunt+")
public record OrganizationRegistrationRequest(
		@NotBlank
		@Size(max = 255)
		@Schema(example = "Instituto Voluntário")
		String organizationName,

		@Pattern(regexp = "\\d{14}")
		@Schema(example = "12345678000195", nullable = true)
		String cnpj) {
}
