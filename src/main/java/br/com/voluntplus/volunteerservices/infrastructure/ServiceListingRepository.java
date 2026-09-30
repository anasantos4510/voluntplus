package br.com.voluntplus.volunteerservices.infrastructure;

import br.com.voluntplus.volunteerservices.domain.ServiceListing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceListingRepository extends JpaRepository<ServiceListing, Long> {
    List<ServiceListing> findByOwnerId(Long ownerId);
}
