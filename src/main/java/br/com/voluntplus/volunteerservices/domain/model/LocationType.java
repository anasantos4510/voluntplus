package br.com.voluntplus.volunteerservices.domain.model;

import br.com.voluntplus.volunteerservices.domain.exception.InvalidVolunteerServiceException;

import java.util.Arrays;

public enum LocationType {
	CASA(1),
	INSTITUICAO(2),
	LOCAL_PUBLICO(5),
	OUTRO(6);

	private final int code;

	LocationType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static LocationType fromCode(int code) {
		return Arrays.stream(values())
				.filter(type -> type.code == code)
				.findFirst()
				.orElseThrow(() -> new InvalidVolunteerServiceException("Unsupported location type: " + code));
	}
}
