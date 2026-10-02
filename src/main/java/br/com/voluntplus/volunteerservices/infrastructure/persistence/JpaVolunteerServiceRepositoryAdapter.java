package br.com.voluntplus.volunteerservices.infrastructure.persistence;

import br.com.voluntplus.volunteerservices.application.port.out.VolunteerServiceRepository;
import br.com.voluntplus.volunteerservices.domain.model.VolunteerService;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaVolunteerServiceRepositoryAdapter implements VolunteerServiceRepository {

	private final SpringDataVolunteerServiceJpaRepository repository;

	public JpaVolunteerServiceRepositoryAdapter(SpringDataVolunteerServiceJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public VolunteerService save(VolunteerService service) {
		VolunteerServiceJpaEntity savedService = repository.saveAndFlush(
				VolunteerServicePersistenceMapper.toEntity(service));
		return VolunteerServicePersistenceMapper.toDomain(savedService);
	}

	@Override
	public Optional<VolunteerService> findById(UUID id) {
		return repository.findById(id).map(VolunteerServicePersistenceMapper::toDomain);
	}

	@Override
	public List<VolunteerService> findAllActiveNotDeleted() {
		return repository.findAllActiveNotDeleted().stream()
				.map(VolunteerServicePersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public Optional<VolunteerService> findActiveNotDeletedById(UUID id) {
		return repository.findActiveNotDeletedById(id)
				.map(VolunteerServicePersistenceMapper::toDomain);
	}

	@Override
	public List<VolunteerService> findAllByOwnerIdAndNotDeleted(UUID ownerId) {
		return repository.findAllByOwnerIdAndNotDeleted(ownerId).stream()
				.map(VolunteerServicePersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public Optional<VolunteerService> findOwnedNotDeletedById(UUID serviceId, UUID ownerId) {
		return repository.findOwnedNotDeletedById(serviceId, ownerId)
				.map(VolunteerServicePersistenceMapper::toDomain);
	}
}
