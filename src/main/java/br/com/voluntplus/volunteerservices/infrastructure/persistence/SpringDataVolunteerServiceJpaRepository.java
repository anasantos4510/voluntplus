package br.com.voluntplus.volunteerservices.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringDataVolunteerServiceJpaRepository extends JpaRepository<VolunteerServiceJpaEntity, UUID> {

	@EntityGraph(attributePaths = {"weekdays", "shifts"})
	@Query("""
			select distinct service
			from VolunteerServiceJpaEntity service
			where service.status = br.com.voluntplus.volunteerservices.domain.model.ServiceStatus.ATIVO
			  and service.deletedAt is null
			order by service.createdAt desc, service.id asc
			""")
	List<VolunteerServiceJpaEntity> findAllActiveNotDeleted();

	@EntityGraph(attributePaths = {"weekdays", "shifts"})
	@Query("""
			select distinct service
			from VolunteerServiceJpaEntity service
			where service.id = :id
			  and service.status = br.com.voluntplus.volunteerservices.domain.model.ServiceStatus.ATIVO
			  and service.deletedAt is null
			""")
	Optional<VolunteerServiceJpaEntity> findActiveNotDeletedById(@Param("id") UUID id);

	@EntityGraph(attributePaths = {"weekdays", "shifts"})
	@Query("""
			select distinct service
			from VolunteerServiceJpaEntity service
			where service.ownerId = :ownerId
			  and service.deletedAt is null
			order by service.createdAt desc, service.id asc
			""")
	List<VolunteerServiceJpaEntity> findAllByOwnerIdAndNotDeleted(@Param("ownerId") UUID ownerId);

	@EntityGraph(attributePaths = {"weekdays", "shifts"})
	@Query("""
			select distinct service
			from VolunteerServiceJpaEntity service
			where service.id = :serviceId
			  and service.ownerId = :ownerId
			  and service.deletedAt is null
			""")
	Optional<VolunteerServiceJpaEntity> findOwnedNotDeletedById(
			@Param("serviceId") UUID serviceId,
			@Param("ownerId") UUID ownerId);
}
