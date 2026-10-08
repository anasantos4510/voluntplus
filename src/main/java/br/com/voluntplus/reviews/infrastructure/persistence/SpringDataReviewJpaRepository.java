package br.com.voluntplus.reviews.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

interface SpringDataReviewJpaRepository extends JpaRepository<ReviewJpaEntity, UUID> {

	List<ReviewJpaEntity> findAllByServiceIdOrderByCreatedAtAscIdAsc(UUID serviceId);

	@Query("""
			select r.serviceId as serviceId,
			       avg(r.rating) as averageRating,
			       count(r.id) as reviewCount
			from ReviewJpaEntity r
			where r.serviceId in :serviceIds
			group by r.serviceId
			""")
	List<ReviewStatisticsProjection> findStatisticsByServiceIds(
			@Param("serviceIds") Set<UUID> serviceIds);

	interface ReviewStatisticsProjection {

		UUID getServiceId();

		Double getAverageRating();

		long getReviewCount();
	}
}
