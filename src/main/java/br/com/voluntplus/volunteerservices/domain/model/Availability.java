package br.com.voluntplus.volunteerservices.domain.model;

import br.com.voluntplus.volunteerservices.domain.exception.InvalidVolunteerServiceException;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class Availability {

	private final Set<Weekday> weekdays;
	private final Set<Shift> shifts;

	public Availability(Collection<Weekday> weekdays, Collection<Shift> shifts) {
		this.weekdays = copyAndValidate(weekdays, "weekdays");
		this.shifts = copyAndValidate(shifts, "shifts");
	}

	public Set<Weekday> getWeekdays() {
		return weekdays;
	}

	public Set<Shift> getShifts() {
		return shifts;
	}

	private static <E extends Enum<E>> Set<E> copyAndValidate(
			Collection<E> values,
			String fieldName) {
		if (values == null || values.isEmpty()) {
			throw new InvalidVolunteerServiceException(fieldName + " must not be empty");
		}
		if (values.stream().anyMatch(value -> value == null)) {
			throw new InvalidVolunteerServiceException(fieldName + " must not contain null values");
		}

		EnumSet<E> uniqueValues = EnumSet.copyOf(values);
		if (uniqueValues.size() != values.size()) {
			throw new InvalidVolunteerServiceException(fieldName + " must not contain duplicate values");
		}
		return Collections.unmodifiableSet(EnumSet.copyOf(uniqueValues));
	}
}
