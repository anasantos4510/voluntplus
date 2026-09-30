package br.com.voluntplus.reviews.application.port.out;

import br.com.voluntplus.reviews.domain.ServiceReview;
import java.util.List;

public interface ReviewStore {
    List<ServiceReview> findByServiceIdOrderByDataCriacaoDesc(Long serviceId);
    ServiceReview save(ServiceReview review);
    void deleteAll(List<ServiceReview> reviews);
}
