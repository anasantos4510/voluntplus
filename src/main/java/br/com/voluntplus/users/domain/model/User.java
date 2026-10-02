package br.com.voluntplus.users.domain.model;

import br.com.voluntplus.users.domain.exception.InvalidUserRegistrationException;
import br.com.voluntplus.users.domain.exception.InvalidUserProfileUpdateException;
import br.com.voluntplus.users.domain.exception.UserRoleAlreadyActiveException;
import br.com.voluntplus.users.domain.exception.UserRoleChangeNotAllowedException;

import java.time.LocalDate;
import java.util.UUID;

public final class User {

	private final UUID id;
	private final String clerkUserId;
	private final PersonType personType;
	private UserRole currentRole;
	private final String email;
	private String fullName;
	private LocalDate birthDate;
	private Gender gender;
	private String organizationName;
	private String cnpj;

	private User(
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
		this.id = requireNonNull(id, "id");
		this.clerkUserId = requireNonBlank(clerkUserId, "clerkUserId");
		this.personType = requireNonNull(personType, "personType");
		this.currentRole = requireNonNull(currentRole, "currentRole");
		this.email = requireNonBlank(email, "email");
		this.fullName = fullName;
		this.birthDate = birthDate;
		this.gender = gender;
		this.organizationName = organizationName;
		this.cnpj = cnpj;

		validateRegistrationData();
	}

	public static User createIndividual(
			UUID id,
			String clerkUserId,
			String fullName,
			LocalDate birthDate,
			String email,
			Gender gender,
			UserRole currentRole,
			LocalDate registrationDate) {
		User user = new User(
				id,
				clerkUserId,
				PersonType.INDIVIDUAL,
				currentRole,
				email,
				fullName,
				birthDate,
				gender,
				null,
				null);
		validateMinimumAge(birthDate, registrationDate);
		return user;
	}

	public static User restore(
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
		return new User(
				id,
				clerkUserId,
				personType,
				currentRole,
				email,
				fullName,
				birthDate,
				gender,
				organizationName,
				cnpj);
	}

	public static User createOrganization(
			UUID id,
			String clerkUserId,
			String organizationName,
			String email) {
		return createOrganization(id, clerkUserId, organizationName, email, null);
	}

	public static User createOrganization(
			UUID id,
			String clerkUserId,
			String organizationName,
			String email,
			String cnpj) {
		return new User(
				id,
				clerkUserId,
				PersonType.ORGANIZATION,
				UserRole.OFFERER,
				email,
				null,
				null,
				null,
				organizationName,
				cnpj);
	}

	public UUID getId() {
		return id;
	}

	public String getClerkUserId() {
		return clerkUserId;
	}

	public PersonType getPersonType() {
		return personType;
	}

	public UserRole getCurrentRole() {
		return currentRole;
	}

	public void changeCurrentRole(UserRole requestedRole) {
		if (personType != PersonType.INDIVIDUAL) {
			throw new UserRoleChangeNotAllowedException("Only an individual can change the current role");
		}

		if (requestedRole == null) {
			throw new UserRoleChangeNotAllowedException("The requested role must not be null");
		}

		if (currentRole == requestedRole) {
			throw new UserRoleAlreadyActiveException(requestedRole);
		}

		currentRole = requestedRole;
	}

	public void updateIndividualProfile(
			String fullName,
			LocalDate birthDate,
			Gender gender,
			LocalDate updateDate) {
		if (personType != PersonType.INDIVIDUAL) {
			throw new InvalidUserProfileUpdateException("Only an individual can update individual profile data");
		}
		if (fullName == null || fullName.isBlank()) {
			throw new InvalidUserProfileUpdateException("fullName must not be blank");
		}
		if (birthDate == null) {
			throw new InvalidUserProfileUpdateException("birthDate must not be null");
		}
		if (gender == null) {
			throw new InvalidUserProfileUpdateException("gender must not be null");
		}
		if (updateDate == null) {
			throw new InvalidUserProfileUpdateException("updateDate must not be null");
		}
		if (birthDate.plusYears(18).isAfter(updateDate)) {
			throw new InvalidUserProfileUpdateException("An individual must be at least 18 years old");
		}

		this.fullName = fullName;
		this.birthDate = birthDate;
		this.gender = gender;
	}

	public void updateOrganizationProfile(String organizationName, String cnpj) {
		if (personType != PersonType.ORGANIZATION) {
			throw new InvalidUserProfileUpdateException("Only an organization can update organization profile data");
		}
		if (organizationName == null || organizationName.isBlank()) {
			throw new InvalidUserProfileUpdateException("organizationName must not be blank");
		}
		if (cnpj != null && !cnpj.matches("\\d{14}")) {
			throw new InvalidUserProfileUpdateException("cnpj must contain exactly 14 digits");
		}

		this.organizationName = organizationName;
		this.cnpj = cnpj;
	}

	public String getEmail() {
		return email;
	}

	public String getFullName() {
		return fullName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public Gender getGender() {
		return gender;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public String getCnpj() {
		return cnpj;
	}

	private void validateRegistrationData() {
		if (personType == PersonType.INDIVIDUAL) {
			requireNonBlank(fullName, "fullName");
			requireNonNull(birthDate, "birthDate");
			requireNonNull(gender, "gender");

			if (organizationName != null || cnpj != null) {
				throw new InvalidUserRegistrationException("An individual cannot have organization registration data");
			}
			return;
		}

		if (currentRole != UserRole.OFFERER) {
			throw new InvalidUserRegistrationException("An organization can only have the OFFERER role");
		}

		requireNonBlank(organizationName, "organizationName");
		if (cnpj != null) {
			validateCnpj(cnpj);
		}

		if (fullName != null || birthDate != null || gender != null) {
			throw new InvalidUserRegistrationException("An organization cannot have individual registration data");
		}
	}

	private static void validateCnpj(String value) {
		requireNonBlank(value, "cnpj");
		if (!value.matches("\\d{14}")) {
			throw new InvalidUserRegistrationException("cnpj must contain exactly 14 digits");
		}
	}

	private static void validateMinimumAge(LocalDate birthDate, LocalDate registrationDate) {
		requireNonNull(birthDate, "birthDate");
		requireNonNull(registrationDate, "registrationDate");

		if (birthDate.plusYears(18).isAfter(registrationDate)) {
			throw new InvalidUserRegistrationException("An individual must be at least 18 years old");
		}
	}

	private static String requireNonBlank(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new InvalidUserRegistrationException(fieldName + " must not be blank");
		}

		return value;
	}

	private static <T> T requireNonNull(T value, String fieldName) {
		if (value == null) {
			throw new InvalidUserRegistrationException(fieldName + " must not be null");
		}

		return value;
	}
}
