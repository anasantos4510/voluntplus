package br.com.voluntplus.volunteerservices.application;

import br.com.voluntplus.volunteerservices.infrastructure.LocalImageStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.Path;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class LocalImageStorageTest {
    @TempDir Path directory;

    @Test void persistsUploadedImageOutsideDatabase() throws Exception {
        LocalImageStorage storage = new LocalImageStorage(directory.toString(), "http://localhost:8080");
        String image = "data:image/png;base64," + Base64.getEncoder()
                .encodeToString(new byte[] {(byte) 0x89, 'P', 'N', 'G', 0, 0, 0, 0});
        String url = storage.storeIfUploaded(image);
        assertTrue(url.startsWith("http://localhost:8080/api/service-images/"));
        assertTrue(java.nio.file.Files.exists(storage.resolve(url.substring(url.lastIndexOf('/') + 1))));
        assertThrows(ResponseStatusException.class, () -> storage.resolve("../.env"));
    }
}
