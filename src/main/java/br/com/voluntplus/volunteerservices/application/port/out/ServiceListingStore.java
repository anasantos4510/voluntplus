package br.com.voluntplus.volunteerservices.application.port.out;

import br.com.voluntplus.volunteerservices.domain.ServiceListing;
import java.util.List;
import java.util.Optional;

public interface ServiceListingStore {
    Optional<ServiceListing> findById(Long id);
    List<ServiceListing> findAll();
    List<ServiceListing> findByOwnerId(Long ownerId);
    ServiceListing save(ServiceListing listing);
    void delete(ServiceListing listing);
}
