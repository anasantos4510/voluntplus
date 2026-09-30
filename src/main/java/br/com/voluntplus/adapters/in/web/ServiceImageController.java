package br.com.voluntplus.adapters.in.web;

import br.com.voluntplus.volunteerservices.infrastructure.LocalImageStorage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceImageController {
    private final LocalImageStorage storage;
    public ServiceImageController(LocalImageStorage storage) { this.storage = storage; }

    @GetMapping("/api/service-images/{filename}")
    public ResponseEntity<FileSystemResource> get(@PathVariable String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        MediaType type = switch (extension) {
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg" -> MediaType.IMAGE_JPEG;
            default -> MediaType.parseMediaType("image/webp");
        };
        return ResponseEntity.ok().contentType(type).body(new FileSystemResource(storage.resolve(filename)));
    }
}
