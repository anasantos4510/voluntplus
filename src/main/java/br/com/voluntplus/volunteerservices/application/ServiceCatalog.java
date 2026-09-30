package br.com.voluntplus.volunteerservices.application;

import br.com.voluntplus.users.UserAccount;
import br.com.voluntplus.users.application.UserAccess;
import br.com.voluntplus.volunteerservices.domain.ServiceListing;
import br.com.voluntplus.volunteerservices.application.port.out.ServiceListingStore;
import br.com.voluntplus.volunteerservices.application.port.out.ImageStorage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@Service
public class ServiceCatalog {
    private final ServiceListingStore services;
    private final UserAccess users;
    private final ImageStorage images;

    public ServiceCatalog(ServiceListingStore services, UserAccess users, ImageStorage images) {
        this.services = services;
        this.users = users;
        this.images = images;
    }

    public ServiceListing get(Long id) {
        return services.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    public List<ServiceListing> list() {
        return services.findAll().stream().filter(listing -> "ATIVO".equals(listing.status))
                .map(this::withProvider).toList();
    }

    public ServiceListing withProvider(ServiceListing listing) {
        listing.usuario = users.providerSummary(listing.ownerId);
        return listing;
    }

    public List<ServiceListing> mine() { return services.findByOwnerId(users.requireRole("OFFERER").id); }

    @Transactional
    public ServiceListing create(Map<String, Object> input) {
        UserAccount offerer = users.requireRole("OFFERER");
        ServiceListing listing = new ServiceListing();
        listing.ownerId = offerer.id;
        apply(listing, input);
        if (listing.name == null || listing.name.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome obrigatório");
        listing.providerImage = images.storeIfUploaded(listing.providerImage);
        return services.save(listing);
    }

    @Transactional
    public ServiceListing update(Long id, Map<String, Object> input) {
        ServiceListing listing = owned(id);
        apply(listing, input);
        if (listing.name == null || listing.name.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome obrigatório");
        listing.providerImage = images.storeIfUploaded(listing.providerImage);
        return services.save(listing);
    }

    @Transactional
    public void delete(Long id) { services.delete(owned(id)); }

    /** Checks the active role and the owner before allowing any mutation. */
    public ServiceListing owned(Long id) {
        UserAccount offerer = users.requireRole("OFFERER");
        ServiceListing listing = get(id);
        if (!listing.ownerId.equals(offerer.id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Serviço de outro ofertante");
        return listing;
    }

    private static String field(Map<String, Object> input, String key, String previous) {
        if (!input.containsKey(key)) return previous;
        Object value = input.get(key);
        if (value == null) return null;
        if (value instanceof java.util.List<?> values)
            return values.stream().map(Object::toString).collect(java.util.stream.Collectors.joining(","));
        return value.toString();
    }

    private static void apply(ServiceListing listing, Map<String, Object> input) {
        listing.name = field(input, "name", listing.name);
        listing.descricao = field(input, "descricao", listing.descricao);
        listing.modalities = field(input, "modalities", listing.modalities);
        listing.idCategoria = field(input, "idCategoria", listing.idCategoria);
        listing.status = field(input, "status", listing.status);
        listing.providerImage = field(input, "providerImage", listing.providerImage);
        listing.diaDaSemana = field(input, "diaDaSemana", listing.diaDaSemana);
        listing.turno = field(input, "turno", listing.turno);
        listing.cep = field(input, "cep", listing.cep);
        listing.estado = field(input, "estado", listing.estado);
        listing.cidade = field(input, "cidade", listing.cidade);
        listing.bairro = field(input, "bairro", listing.bairro);
        listing.tipoLocalizacao = field(input, "tipoLocalizacao", listing.tipoLocalizacao);
        listing.whatsapp = field(input, "whatsapp", listing.whatsapp);
        listing.telefone = field(input, "telefone", listing.telefone);
        listing.instagram = field(input, "instagram", listing.instagram);
        listing.site = field(input, "site", listing.site);
        if (!"ATIVO".equals(listing.status) && !"INATIVO".equals(listing.status))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido");
        if (input.containsKey("tipoLocalizacao") && !"ONLINE".equals(listing.modalities) &&
                !java.util.Set.of("1", "2", "5", "6").contains(listing.tipoLocalizacao == null ? "" : listing.tipoLocalizacao))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de localização inválido");
        if ("ONLINE".equals(listing.modalities)) listing.tipoLocalizacao = null;
    }
}
