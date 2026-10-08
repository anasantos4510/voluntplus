package br.com.voluntplus.reviews.infrastructure.persistence;

import br.com.voluntplus.reviews.application.port.out.ReviewRepository;
import br.com.voluntplus.reviews.domain.model.Review;
import br.com.voluntplus.volunteerservices.api.ServiceReviewStatistics;
import br.com.voluntplus.volunteerservices.api.ServiceReviewStatisticsProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaReviewRepositoryAdapter implements ReviewRepository, ServiceReviewStatisticsProvider {

	private final SpringDataReviewJpaRepository repository;

	public JpaReviewRepositoryAdapter(SpringDataReviewJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Review save(Review review) {
		ReviewJpaEntity savedReview = repository.saveAndFlush(ReviewPersistenceMapper.toEntity(review));
		return ReviewPersistenceMapper.toDomain(savedReview);
	}

	@Override
	public List<Review> findAllByServiceIdOrderByCreatedAtAscIdAsc(UUID serviceId) {
		return repository.findAllByServiceIdOrderByCreatedAtAscIdAsc(serviceId).stream()
				.map(ReviewPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public Map<UUID, ServiceReviewStatistics> findByServiceIds(Set<UUID> serviceIds) {
		if (serviceIds.isEmpty()) {
			return Map.of();
		}

		return repository.findStatisticsByServiceIds(serviceIds).stream()
				.collect(Collectors.toUnmodifiableMap(
						SpringDataReviewJpaRepository.ReviewStatisticsProjection::getServiceId,
						statistics -> new ServiceReviewStatistics(
								statistics.getServiceId(),
								roundToOneDecimal(statistics.getAverageRating()),
								statistics.getReviewCount())));
	}

	private double roundToOneDecimal(double value) {
		return Math.round(value * 10.0) / 10.0;
	}
}
