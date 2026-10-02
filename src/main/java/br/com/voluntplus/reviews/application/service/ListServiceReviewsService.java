package br.com.voluntplus.reviews.application.service;

import br.com.voluntplus.reviews.application.exception.ReviewableServiceNotFoundException;
import br.com.voluntplus.reviews.application.port.in.ListServiceReviewsUseCase;
import br.com.voluntplus.reviews.application.port.in.ReviewResult;
import br.com.voluntplus.reviews.application.port.out.ReviewRepository;
import br.com.voluntplus.reviews.domain.model.Review;
import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.volunteerservices.api.ServiceReviewContext;
import br.com.voluntplus.volunteerservices.api.VolunteerServicesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListServiceReviewsService implements ListServiceReviewsUseCase {

	private final VolunteerServicesApi volunteerServicesApi;
	private final UsersApi usersApi;
	private final ReviewRepository repository;

	public ListServiceReviewsService(
			VolunteerServicesApi volunteerServicesApi,
			UsersApi usersApi,
			ReviewRepository repository) {
		this.volunteerServicesApi = volunteerServicesApi;
		this.usersApi = usersApi;
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewResult> listByServiceId(UUID serviceId) {
		requireReviewableService(serviceId);
		List<Review> reviews = repository.findAllByServiceIdOrderByCreatedAtAscIdAsc(serviceId);
		if (reviews.isEmpty()) {
			return List.of();
		}

		Set<UUID> authorIds = reviews.stream()
				.map(Review::getAuthorId)
				.collect(Collectors.toUnmodifiableSet());
		Map<UUID, UserSummary> authors = usersApi.findAllByIds(authorIds);

		return reviews.stream()
				.map(review -> new ReviewResult(review, resolveAuthorName(authors.get(review.getAuthorId()))))
				.toList();
	}

	private void requireReviewableService(UUID serviceId) {
		ServiceReviewContext context = volunteerServicesApi.findReviewContext(serviceId)
				.orElseThrow(() -> new ReviewableServiceNotFoundException(serviceId));
		if (!context.active() || context.deleted()) {
			throw new ReviewableServiceNotFoundException(serviceId);
		}
	}

	private String resolveAuthorName(UserSummary author) {
		return author == null ? null : ReviewAuthorName.from(author);
	}
}
