package br.com.voluntplus.reviews.infrastructure;

import br.com.voluntplus.reviews.domain.ServiceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceReviewRepository extends JpaRepository<ServiceReview, Long> {
    List<ServiceReview> findByServiceIdOrderByDataCriacaoDesc(Long serviceId);
}
