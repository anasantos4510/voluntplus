package br.com.voluntplus.volunteerservices.application;

import br.com.voluntplus.reviews.application.ReviewCatalog;
import br.com.voluntplus.reviews.application.port.out.ReviewStore;
import br.com.voluntplus.users.UserAccount;
import br.com.voluntplus.users.application.UserAccess;
import br.com.voluntplus.volunteerservices.application.ServiceCatalog;
import br.com.voluntplus.volunteerservices.application.port.out.ServiceListingStore;
import br.com.voluntplus.volunteerservices.application.port.out.ImageStorage;
import br.com.voluntplus.volunteerservices.domain.ServiceListing;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ModulePermissionsTest {
    private final ServiceListingStore listings = mock(ServiceListingStore.class);
    private final ReviewStore reviews = mock(ReviewStore.class);
    private final UserAccess users = mock(UserAccess.class);
    private final ImageStorage images = mock(ImageStorage.class);
    private final ServiceCatalog services = new ServiceCatalog(listings, users, images);
    private final ReviewCatalog reviewCatalog = new ReviewCatalog(reviews, services, users);

    private void as(Long id, String role) {
        UserAccount account = new UserAccount();
        account.id = id;
        account.currentRole = role;
        when(users.requireRole(role)).thenReturn(account);
        String other = role.equals("OFFERER") ? "BENEFICIARY" : "OFFERER";
        when(users.requireRole(other)).thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));
    }

    @Test void offererCannotReview() {
        as(1L, "OFFERER");
        assertThrows(ResponseStatusException.class,
                () -> reviewCatalog.create(12L, new ReviewCatalog.ReviewInput(5, "Ótimo")));
        verify(reviews, never()).save(any());
    }

    @Test void beneficiaryCannotCreateService() {
        as(1L, "BENEFICIARY");
        assertThrows(ResponseStatusException.class, () -> services.create(Map.of("name", "Aula")));
        verify(listings, never()).save(any());
    }

    @Test void beneficiaryCannotReviewOwnService() {
        as(1L, "BENEFICIARY");
        ServiceListing listing = new ServiceListing();
        listing.id = 12L;
        listing.ownerId = 1L;
        when(listings.findById(12L)).thenReturn(Optional.of(listing));
        assertThrows(ResponseStatusException.class,
                () -> reviewCatalog.create(12L, new ReviewCatalog.ReviewInput(5, "Ótimo")));
        verify(reviews, never()).save(any());
    }

    @Test void offererCannotEditAnotherPersonsService() {
        as(1L, "OFFERER");
        ServiceListing listing = new ServiceListing();
        listing.id = 12L;
        listing.ownerId = 2L;
        when(listings.findById(12L)).thenReturn(Optional.of(listing));
        assertThrows(ResponseStatusException.class,
                () -> services.update(12L, Map.of("name", "Alterado")));
        verify(listings, never()).save(any());
    }

    @Test void publicCatalogExcludesInactiveServicesWithoutDeletingThem() {
        ServiceListing active = new ServiceListing(); active.id = 1L; active.ownerId = 7L;
        ServiceListing inactive = new ServiceListing(); inactive.id = 2L; inactive.ownerId = 7L; inactive.status = "INATIVO";
        when(listings.findAll()).thenReturn(java.util.List.of(active, inactive));
        when(users.providerSummary(7L)).thenReturn(Map.of("fullName", "Ana", "idade", 31));
        assertEquals(java.util.List.of(active), services.list());
        assertEquals("Ana", active.usuario.get("fullName"));
        verify(listings, never()).delete(any());
    }

    @Test void multipleDaysRemainIndividuallyReadableAfterSaving() {
        as(7L, "OFFERER");
        when(listings.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ServiceListing result = services.create(Map.of("name", "Aula", "diaDaSemana", java.util.List.of("TERCA", "QUINTA"), "tipoLocalizacao", "2"));
        assertEquals("TERCA,QUINTA", result.diaDaSemana);
        assertEquals("2", result.tipoLocalizacao);
    }

    @Test void savedReviewsIncludeAuthorsName() {
        ServiceListing listing = new ServiceListing(); listing.id = 12L; listing.ownerId = 7L;
        when(listings.findById(12L)).thenReturn(Optional.of(listing));
        br.com.voluntplus.reviews.domain.ServiceReview review = new br.com.voluntplus.reviews.domain.ServiceReview();
        review.authorId = 3L;
        when(reviews.findByServiceIdOrderByDataCriacaoDesc(12L)).thenReturn(java.util.List.of(review));
        when(users.displayName(3L)).thenReturn("Ana Silva");
        assertEquals("Ana Silva", reviewCatalog.list(12L).get(0).nomeAutor);
    }
}
