package br.com.voluntplus.reviews.infrastructure;

import br.com.voluntplus.reviews.application.port.out.ReviewStore;
import br.com.voluntplus.reviews.domain.ServiceReview;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JpaReviewStore implements ReviewStore {
    private final ServiceReviewRepository repository;

    public JpaReviewStore(ServiceReviewRepository repository) { this.repository = repository; }
    public List<ServiceReview> findByServiceIdOrderByDataCriacaoDesc(Long serviceId) {
        return repository.findByServiceIdOrderByDataCriacaoDesc(serviceId);
    }
    public ServiceReview save(ServiceReview review) { return repository.save(review); }
    public void deleteAll(List<ServiceReview> reviews) { repository.deleteAll(reviews); }
}
