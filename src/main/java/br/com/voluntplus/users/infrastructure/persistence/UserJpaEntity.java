package br.com.voluntplus.users.infrastructure.persistence;

import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
class UserJpaEntity {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "clerk_user_id", nullable = false, unique = true, length = 255)
	private String clerkUserId;

	@Enumerated(EnumType.STRING)
	@Column(name = "person_type", nullable = false, length = 20)
	private PersonType personType;

	@Enumerated(EnumType.STRING)
	@Column(name = "current_user_role", nullable = false, length = 20)
	private UserRole currentRole;

	@Column(name = "email", nullable = false, length = 320)
	private String email;

	@Column(name = "full_name", length = 255)
	private String fullName;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 32)
	private Gender gender;

	@Column(name = "organization_name", length = 255)
	private String organizationName;

	@Column(name = "cnpj", length = 14)
	private String cnpj;

	protected UserJpaEntity() {
	}

	UserJpaEntity(
			UUID id,
			String clerkUserId,
			PersonType personType,
			UserRole currentRole,
			String email,
			String fullName,
			LocalDate birthDate,
			Gender gender,
			String organizationName,
			String cnpj) {
		this.id = id;
		this.clerkUserId = clerkUserId;
		this.personType = personType;
		this.currentRole = currentRole;
		this.email = email;
		this.fullName = fullName;
		this.birthDate = birthDate;
		this.gender = gender;
		this.organizationName = organizationName;
		this.cnpj = cnpj;
	}

	UUID getId() {
		return id;
	}

	String getClerkUserId() {
		return clerkUserId;
	}

	PersonType getPersonType() {
		return personType;
	}

	UserRole getCurrentRole() {
		return currentRole;
	}

	String getEmail() {
		return email;
	}

	String getFullName() {
		return fullName;
	}

	LocalDate getBirthDate() {
		return birthDate;
	}

	Gender getGender() {
		return gender;
	}

	String getOrganizationName() {
		return organizationName;
	}

	String getCnpj() {
		return cnpj;
	}
}
