package br.com.voluntplus.volunteerservices.domain.model;

import br.com.voluntplus.volunteerservices.domain.exception.InvalidVolunteerServiceException;

public final class ServiceContacts {

	private final String whatsapp;
	private final String phone;
	private final String instagram;
	private final String website;

	public ServiceContacts(String whatsapp, String phone, String instagram, String website) {
		this.whatsapp = normalizePhone(whatsapp, "whatsapp");
		this.phone = normalizePhone(phone, "phone");
		this.instagram = normalizeOptional(instagram);
		this.website = normalizeOptional(website);

		if (this.whatsapp == null && this.phone == null && this.instagram == null && this.website == null) {
			throw new InvalidVolunteerServiceException("At least one service contact must be provided");
		}
	}

	public String getWhatsapp() {
		return whatsapp;
	}

	public String getPhone() {
		return phone;
	}

	public String getInstagram() {
		return instagram;
	}

	public String getWebsite() {
		return website;
	}

	private static String normalizePhone(String value, String fieldName) {
		String normalized = normalizeOptional(value);
		if (normalized == null) {
			return null;
		}

		String digits = normalized.replaceAll("\\D", "");
		if (digits.length() < 10 || digits.length() > 11) {
			throw new InvalidVolunteerServiceException(fieldName + " must contain 10 or 11 digits");
		}
		return digits;
	}

	private static String normalizeOptional(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}
}
