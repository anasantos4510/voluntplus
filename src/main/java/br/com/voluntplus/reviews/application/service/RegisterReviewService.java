package br.com.voluntplus.reviews.application.service;

import br.com.voluntplus.reviews.application.exception.OwnServiceReviewNotAllowedException;
import br.com.voluntplus.reviews.application.exception.ReviewableServiceNotFoundException;
import br.com.voluntplus.reviews.application.port.in.RegisterReviewUseCase;
import br.com.voluntplus.reviews.application.port.in.ReviewResult;
import br.com.voluntplus.reviews.application.port.out.ReviewRepository;
import br.com.voluntplus.reviews.domain.model.Review;
import br.com.voluntplus.users.api.CurrentRole;
import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.volunteerservices.api.ServiceReviewContext;
import br.com.voluntplus.volunteerservices.api.VolunteerServicesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class RegisterReviewService implements RegisterReviewUseCase {

	private final UsersApi usersApi;
	private final VolunteerServicesApi volunteerServicesApi;
	private final ReviewRepository repository;
	private final Clock clock;

	public RegisterReviewService(
			UsersApi usersApi,
			VolunteerServicesApi volunteerServicesApi,
			ReviewRepository repository,
			Clock clock) {
		this.usersApi = usersApi;
		this.volunteerServicesApi = volunteerServicesApi;
		this.repository = repository;
		this.clock = clock;
	}

	@Override
	@Transactional
	public ReviewResult register(RegisterReviewCommand command) {
		UserSummary author = usersApi.requireCurrentUserWithRole(CurrentRole.BENEFICIARY);
		ServiceReviewContext service = requireReviewableService(command.serviceId());
		if (service.ownerId().equals(author.userId())) {
			throw new OwnServiceReviewNotAllowedException();
		}

		Review review = Review.create(
				UUID.randomUUID(),
				service.serviceId(),
				author.userId(),
				command.rating(),
				command.comment(),
				Instant.now(clock));

		return new ReviewResult(repository.save(review), ReviewAuthorName.from(author));
	}

	private ServiceReviewContext requireReviewableService(UUID serviceId) {
		ServiceReviewContext context = volunteerServicesApi.findReviewContext(serviceId)
				.orElseThrow(() -> new ReviewableServiceNotFoundException(serviceId));
		if (!context.active() || context.deleted()) {
			throw new ReviewableServiceNotFoundException(serviceId);
		}
		return context;
	}
}
