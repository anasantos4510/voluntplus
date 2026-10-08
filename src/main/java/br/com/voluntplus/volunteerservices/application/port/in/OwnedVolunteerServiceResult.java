package br.com.voluntplus.volunteerservices.application.port.in;

import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;

public record OwnedVolunteerServiceResult(
		VolunteerService service,
		UserSummary owner) {
}
