package br.com.voluntplus.volunteerservices.infrastructure;

import br.com.voluntplus.volunteerservices.application.port.out.ServiceListingStore;
import br.com.voluntplus.volunteerservices.domain.ServiceListing;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaServiceListingStore implements ServiceListingStore {
    private final ServiceListingRepository repository;

    public JpaServiceListingStore(ServiceListingRepository repository) { this.repository = repository; }
    public Optional<ServiceListing> findById(Long id) { return repository.findById(id); }
    public List<ServiceListing> findAll() { return repository.findAll(); }
    public List<ServiceListing> findByOwnerId(Long ownerId) { return repository.findByOwnerId(ownerId); }
    public ServiceListing save(ServiceListing listing) { return repository.save(listing); }
    public void delete(ServiceListing listing) { repository.delete(listing); }
}
