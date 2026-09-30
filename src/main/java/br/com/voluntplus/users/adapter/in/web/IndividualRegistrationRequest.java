package br.com.voluntplus.users.adapter.in.web;

import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Dados da pessoa física usados para criar o perfil no Volunt+")
public record IndividualRegistrationRequest(
		@NotBlank
		@Size(max = 255)
		@Schema(example = "Ana Silva")
		String fullName,

		@NotNull
		@Schema(example = "1995-05-20", type = "string", format = "date")
		LocalDate birthDate,

		@NotNull
		@Schema(example = "FEMALE")
		Gender gender,

		@NotNull
		@Schema(example = "BENEFICIARY")
		UserRole initialRole) {
}
