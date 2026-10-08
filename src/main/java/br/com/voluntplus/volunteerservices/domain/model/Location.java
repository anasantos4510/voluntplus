package br.com.voluntplus.volunteerservices.domain.model;

import br.com.voluntplus.volunteerservices.domain.exception.InvalidVolunteerServiceException;

public final class Location {

	private final String postalCode;
	private final String state;
	private final String city;
	private final String neighborhood;
	private final LocationType type;

	public Location(String postalCode, String state, String city, String neighborhood, LocationType type) {
		this.postalCode = requireNonBlank(postalCode, "postalCode");
		this.state = requireNonBlank(state, "state");
		this.city = requireNonBlank(city, "city");
		this.neighborhood = requireNonBlank(neighborhood, "neighborhood");
		if (type == null) {
			throw new InvalidVolunteerServiceException("type must not be null");
		}
		this.type = type;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getState() {
		return state;
	}

	public String getCity() {
		return city;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public LocationType getType() {
		return type;
	}

	private static String requireNonBlank(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new InvalidVolunteerServiceException(fieldName + " must not be blank");
		}
		return value.trim();
	}
}
