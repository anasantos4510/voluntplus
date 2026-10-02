package br.com.voluntplus.volunteerservices.domain.model;

import br.com.voluntplus.volunteerservices.domain.exception.InvalidVolunteerServiceException;
import br.com.voluntplus.volunteerservices.domain.exception.ServiceStatusAlreadySetException;
import br.com.voluntplus.volunteerservices.domain.exception.VolunteerServiceStateException;

import java.time.Instant;
import java.util.UUID;

public final class VolunteerService {

	private final UUID id;
	private final UUID ownerId;
	private String name;
	private String description;
	private ServiceCategory category;
	private ServiceModality modality;
	private ServiceStatus status;
	private Location location;
	private Availability availability;
	private ServiceContacts contacts;
	private ServiceImage image;
	private final Instant createdAt;
	private Instant updatedAt;
	private Instant deletedAt;

	private VolunteerService(
			UUID id,
			UUID ownerId,
			String name,
			String description,
			ServiceCategory category,
			ServiceModality modality,
			ServiceStatus status,
			Location location,
			Availability availability,
			ServiceContacts contacts,
			ServiceImage image,
			Instant createdAt,
			Instant updatedAt,
			Instant deletedAt) {
		this.id = requireNonNull(id, "id");
		this.ownerId = requireNonNull(ownerId, "ownerId");
		this.name = requireNonBlank(name, "name");
		this.description = validateDescription(description);
		this.category = requireNonNull(category, "category");
		this.modality = requireNonNull(modality, "modality");
		this.status = requireNonNull(status, "status");
		this.location = normalizeLocation(modality, location);
		this.availability = availability;
		this.contacts = requireNonNull(contacts, "contacts");
		this.image = image;
		this.createdAt = requireNonNull(createdAt, "createdAt");
		this.updatedAt = requireNonNull(updatedAt, "updatedAt");
		this.deletedAt = deletedAt;
		validateDates();
	}

	public static VolunteerService create(
			UUID id,
			UUID ownerId,
			String name,
			String description,
			ServiceCategory category,
			ServiceModality modality,
			Location location,
			Availability availability,
			ServiceContacts contacts,
			ServiceImage image,
			Instant createdAt) {
		return new VolunteerService(
				id,
				ownerId,
				name,
				description,
				category,
				modality,
				ServiceStatus.ATIVO,
				location,
				availability,
				contacts,
				image,
				createdAt,
				createdAt,
				null);
	}

	public static VolunteerService restore(
			UUID id,
			UUID ownerId,
			String name,
			String description,
			ServiceCategory category,
			ServiceModality modality,
			ServiceStatus status,
			Location location,
			Availability availability,
			ServiceContacts contacts,
			ServiceImage image,
			Instant createdAt,
			Instant updatedAt,
			Instant deletedAt) {
		return new VolunteerService(
				id,
				ownerId,
				name,
				description,
				category,
				modality,
				status,
				location,
				availability,
				contacts,
				image,
				createdAt,
				updatedAt,
				deletedAt);
	}

	public void edit(
			String name,
			String description,
			ServiceCategory category,
			ServiceModality modality,
			Location location,
			Availability availability,
			ServiceContacts contacts,
			ServiceImage image,
			Instant occurredAt) {
		requireNotDeleted();
		if (status != ServiceStatus.ATIVO) {
			throw new VolunteerServiceStateException("Only an active volunteer service can be edited");
		}
		requireChronological(occurredAt);

		VolunteerService candidate = new VolunteerService(
				id,
				ownerId,
				name,
				description,
				category,
				modality,
				status,
				location,
				availability,
				contacts,
				image,
				createdAt,
				occurredAt,
				null);

		this.name = candidate.name;
		this.description = candidate.description;
		this.category = candidate.category;
		this.modality = candidate.modality;
		this.location = candidate.location;
		this.availability = candidate.availability;
		this.contacts = candidate.contacts;
		this.image = candidate.image;
		this.updatedAt = occurredAt;
	}

	public void activate(Instant occurredAt) {
		changeStatus(ServiceStatus.ATIVO, occurredAt);
	}

	public void inactivate(Instant occurredAt) {
		changeStatus(ServiceStatus.INATIVO, occurredAt);
	}

	public void delete(Instant occurredAt) {
		requireNotDeleted();
		requireChronological(occurredAt);
		deletedAt = occurredAt;
		updatedAt = occurredAt;
	}

	public UUID getId() {
		return id;
	}

	public UUID getOwnerId() {
		return ownerId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ServiceCategory getCategory() {
		return category;
	}

	public ServiceModality getModality() {
		return modality;
	}

	public ServiceStatus getStatus() {
		return status;
	}

	public Location getLocation() {
		return location;
	}

	public Availability getAvailability() {
		return availability;
	}

	public ServiceContacts getContacts() {
		return contacts;
	}

	public ServiceImage getImage() {
		return image;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}

	private void changeStatus(ServiceStatus requestedStatus, Instant occurredAt) {
		requireNotDeleted();
		if (status == requestedStatus) {
			throw new ServiceStatusAlreadySetException(requestedStatus);
		}
		requireChronological(occurredAt);
		status = requestedStatus;
		updatedAt = occurredAt;
	}

	private void requireNotDeleted() {
		if (isDeleted()) {
			throw new VolunteerServiceStateException("A deleted volunteer service cannot be changed");
		}
	}

	private void requireChronological(Instant occurredAt) {
		requireNonNull(occurredAt, "occurredAt");
		if (occurredAt.isBefore(updatedAt)) {
			throw new InvalidVolunteerServiceException("Operation date must not be before updatedAt");
		}
	}

	private void validateDates() {
		if (updatedAt.isBefore(createdAt)) {
			throw new InvalidVolunteerServiceException("updatedAt must not be before createdAt");
		}
		if (deletedAt != null && (deletedAt.isBefore(createdAt) || deletedAt.isAfter(updatedAt))) {
			throw new InvalidVolunteerServiceException("deletedAt must be between createdAt and updatedAt");
		}
	}

	private static Location normalizeLocation(ServiceModality modality, Location location) {
		if (modality == ServiceModality.ONLINE) {
			return null;
		}
		if (location == null) {
			throw new InvalidVolunteerServiceException("Location is required for in-person and hybrid services");
		}
		return location;
	}

	private static String validateDescription(String value) {
		String description = requireNonBlank(value, "description");
		if (description.length() < 30) {
			throw new InvalidVolunteerServiceException("description must contain at least 30 characters");
		}
		return description;
	}

	private static String requireNonBlank(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new InvalidVolunteerServiceException(fieldName + " must not be blank");
		}
		return value.trim();
	}

	private static <T> T requireNonNull(T value, String fieldName) {
		if (value == null) {
			throw new InvalidVolunteerServiceException(fieldName + " must not be null");
		}
		return value;
	}
}
