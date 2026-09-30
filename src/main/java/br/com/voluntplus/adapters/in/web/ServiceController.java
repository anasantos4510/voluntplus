package br.com.voluntplus.adapters.in.web;

import br.com.voluntplus.reviews.application.ReviewCatalog;
import br.com.voluntplus.reviews.application.ReviewCatalog.ReviewInput;
import br.com.voluntplus.reviews.domain.ServiceReview;
import br.com.voluntplus.volunteerservices.application.ServiceCatalog;
import br.com.voluntplus.volunteerservices.domain.ServiceListing;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/** HTTP adapter composes the service and review public application interfaces. */
@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ServiceCatalog services;
    private final ReviewCatalog reviews;

    public ServiceController(ServiceCatalog services, ReviewCatalog reviews) {
        this.services = services;
        this.reviews = reviews;
    }

    @GetMapping
    public List<ServiceListing> list() {
        List<ServiceListing> listings = services.list();
        listings.forEach(listing -> listing.avaliacoes = reviews.list(listing.id));
        return listings;
    }

    @GetMapping("/{id}")
    public ServiceListing get(@PathVariable Long id) {
        ServiceListing listing = services.withProvider(services.get(id));
        listing.avaliacoes = reviews.list(id);
        return listing;
    }

    @GetMapping("/mine")
    public List<ServiceListing> mine() { return services.mine(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceListing create(@RequestBody Map<String, Object> input) { return services.create(input); }

    @PatchMapping("/{id}")
    public ServiceListing update(@PathVariable Long id, @RequestBody Map<String, Object> input) {
        return services.update(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable Long id) {
        services.owned(id);
        reviews.deleteByService(id);
        services.delete(id);
    }

    @GetMapping("/{id}/reviews")
    public List<ServiceReview> listReviews(@PathVariable Long id) { return reviews.list(id); }

    @PostMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceReview review(@PathVariable Long id, @Valid @RequestBody ReviewInput input) {
        return reviews.create(id, input);
    }
}
