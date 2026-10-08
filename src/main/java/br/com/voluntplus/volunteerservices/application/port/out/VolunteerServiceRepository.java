package br.com.voluntplus.volunteerservices.application.port.out;

import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VolunteerServiceRepository {

	VolunteerService save(VolunteerService service);

	Optional<VolunteerService> findById(UUID id);

	List<VolunteerService> findAllActiveNotDeleted();

	Optional<VolunteerService> findActiveNotDeletedById(UUID id);

	List<VolunteerService> findAllByOwnerIdAndNotDeleted(UUID ownerId);

	Optional<VolunteerService> findOwnedNotDeletedById(UUID serviceId, UUID ownerId);
}
