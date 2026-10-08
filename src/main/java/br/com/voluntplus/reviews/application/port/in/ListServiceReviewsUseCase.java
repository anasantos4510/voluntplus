package br.com.voluntplus.reviews.application.port.in;

import java.util.List;
import java.util.UUID;

public interface ListServiceReviewsUseCase {

	List<ReviewResult> listByServiceId(UUID serviceId);
}
