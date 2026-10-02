package br.com.voluntplus.reviews.application.port.out;

import br.com.voluntplus.reviews.domain.model.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository {

	Review save(Review review);

	List<Review> findAllByServiceIdOrderByCreatedAtAscIdAsc(UUID serviceId);
}
