package br.com.voluntplus.reviews.application;

import br.com.voluntplus.reviews.domain.ServiceReview;
import br.com.voluntplus.reviews.application.port.out.ReviewStore;
import br.com.voluntplus.users.UserAccount;
import br.com.voluntplus.users.application.UserAccess;
import br.com.voluntplus.volunteerservices.application.ServiceCatalog;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ReviewCatalog {
    public record ReviewInput(@Min(1) @Max(5) int nota, @NotBlank String comentario) {}

    private final ReviewStore reviews;
    private final ServiceCatalog services;
    private final UserAccess users;

    public ReviewCatalog(ReviewStore reviews, ServiceCatalog services, UserAccess users) {
        this.reviews = reviews;
        this.services = services;
        this.users = users;
    }

    public List<ServiceReview> list(Long serviceId) {
        services.get(serviceId);
        List<ServiceReview> result = reviews.findByServiceIdOrderByDataCriacaoDesc(serviceId);
        result.forEach(review -> review.nomeAutor = users.displayName(review.authorId));
        return result;
    }

    @Transactional
    public ServiceReview create(Long serviceId, ReviewInput input) {
        UserAccount beneficiary = users.requireRole("BENEFICIARY");
        if (services.get(serviceId).ownerId.equals(beneficiary.id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode avaliar seu próprio serviço");
        }
        if (input.comentario().trim().length() > 500)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comentário muito longo");
        ServiceReview review = new ServiceReview();
        review.serviceId = serviceId;
        review.authorId = beneficiary.id;
        review.nomeAutor = users.displayName(beneficiary.id);
        review.nota = input.nota();
        review.comentario = input.comentario().trim();
        return reviews.save(review);
    }

    @Transactional
    public void deleteByService(Long serviceId) {
        reviews.deleteAll(reviews.findByServiceIdOrderByDataCriacaoDesc(serviceId));
    }
}
