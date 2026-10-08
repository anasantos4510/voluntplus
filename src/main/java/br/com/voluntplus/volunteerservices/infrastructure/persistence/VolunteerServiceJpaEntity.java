package br.com.voluntplus.volunteerservices.infrastructure.persistence;

import br.com.voluntplus.volunteerservices.domain.model.ServiceCategory;
import br.com.voluntplus.volunteerservices.domain.model.ServiceModality;
import br.com.voluntplus.volunteerservices.domain.model.ServiceStatus;
import br.com.voluntplus.volunteerservices.domain.model.Shift;
import br.com.voluntplus.volunteerservices.domain.model.Weekday;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "volunteer_services")
class VolunteerServiceJpaEntity {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "owner_id", nullable = false, updatable = false)
	private UUID ownerId;

	@Column(name = "name", nullable = false, columnDefinition = "TEXT")
	private String name;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, length = 32)
	private ServiceCategory category;

	@Enumerated(EnumType.STRING)
	@Column(name = "modality", nullable = false, length = 16)
	private ServiceModality modality;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private ServiceStatus status;

	@Column(name = "postal_code", length = 32)
	private String postalCode;

	@Column(name = "state", length = 64)
	private String state;

	@Column(name = "city", length = 255)
	private String city;

	@Column(name = "neighborhood", length = 255)
	private String neighborhood;

	@Column(name = "location_type")
	private Short locationType;

	@Column(name = "whatsapp", length = 11)
	private String whatsapp;

	@Column(name = "phone", length = 11)
	private String phone;

	@Column(name = "instagram", length = 255)
	private String instagram;

	@Column(name = "website", length = 2048)
	private String website;

	@Column(name = "image_data_url", columnDefinition = "TEXT")
	private String imageDataUrl;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "volunteer_service_weekdays",
			joinColumns = @JoinColumn(name = "service_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "weekday", nullable = false, length = 16)
	private Set<Weekday> weekdays = new LinkedHashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "volunteer_service_shifts",
			joinColumns = @JoinColumn(name = "service_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "shift", nullable = false, length = 16)
	private Set<Shift> shifts = new LinkedHashSet<>();

	protected VolunteerServiceJpaEntity() {
	}

	VolunteerServiceJpaEntity(
			UUID id,
			UUID ownerId,
			String name,
			String description,
			ServiceCategory category,
			ServiceModality modality,
			ServiceStatus status,
			String postalCode,
			String state,
			String city,
			String neighborhood,
			Integer locationType,
			String whatsapp,
			String phone,
			String instagram,
			String website,
			String imageDataUrl,
			Instant createdAt,
			Instant updatedAt,
			Instant deletedAt,
			Set<Weekday> weekdays,
			Set<Shift> shifts) {
		this.id = id;
		this.ownerId = ownerId;
		this.name = name;
		this.description = description;
		this.category = category;
		this.modality = modality;
		this.status = status;
		this.postalCode = postalCode;
		this.state = state;
		this.city = city;
		this.neighborhood = neighborhood;
		this.locationType = locationType == null
				? null
				: locationType.shortValue();
		this.whatsapp = whatsapp;
		this.phone = phone;
		this.instagram = instagram;
		this.website = website;
		this.imageDataUrl = imageDataUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.weekdays = new LinkedHashSet<>(weekdays);
		this.shifts = new LinkedHashSet<>(shifts);
	}

	UUID getId() { return id; }
	UUID getOwnerId() { return ownerId; }
	String getName() { return name; }
	String getDescription() { return description; }
	ServiceCategory getCategory() { return category; }
	ServiceModality getModality() { return modality; }
	ServiceStatus getStatus() { return status; }
	String getPostalCode() { return postalCode; }
	String getState() { return state; }
	String getCity() { return city; }
	String getNeighborhood() { return neighborhood; }
	Integer getLocationType() {
		return locationType == null
				? null
				: locationType.intValue();
	}
	String getWhatsapp() { return whatsapp; }
	String getPhone() { return phone; }
	String getInstagram() { return instagram; }
	String getWebsite() { return website; }
	String getImageDataUrl() { return imageDataUrl; }
	Instant getCreatedAt() { return createdAt; }
	Instant getUpdatedAt() { return updatedAt; }
	Instant getDeletedAt() { return deletedAt; }
	Set<Weekday> getWeekdays() { return Set.copyOf(weekdays); }
	Set<Shift> getShifts() { return Set.copyOf(shifts); }
}
