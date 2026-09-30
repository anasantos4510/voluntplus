package br.com.voluntplus.volunteerservices.infrastructure;

import br.com.voluntplus.volunteerservices.application.port.out.ImageStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Component
public class LocalImageStorage implements ImageStorage {
    private static final int MAX_BYTES = 5 * 1024 * 1024;
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/png", "png", "image/jpeg", "jpg", "image/webp", "webp");
    private final Path directory;
    private final String publicBaseUrl;

    public LocalImageStorage(@Value("${app.images.directory:./data/images}") String directory,
                             @Value("${app.public-base-url:http://localhost:8080}") String publicBaseUrl) {
        this.directory = Path.of(directory).toAbsolutePath().normalize();
        this.publicBaseUrl = publicBaseUrl.replaceAll("/+$", "");
    }

    @Override
    public String storeIfUploaded(String image) {
        if (image == null || !image.startsWith("data:")) return image;
        int separator = image.indexOf(";base64,");
        if (separator < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem inválida");
        String extension = EXTENSIONS.get(image.substring(5, separator));
        if (extension == null || image.length() > MAX_BYTES * 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato ou tamanho de imagem inválido");
        try {
            byte[] bytes = Base64.getDecoder().decode(image.substring(separator + 8));
            if (bytes.length > MAX_BYTES || bytes.length == 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem deve ter até 5 MB");
            boolean valid = switch (extension) {
                case "png" -> bytes.length >= 8 && (bytes[0] & 0xff) == 0x89
                        && bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G';
                case "jpg" -> bytes.length >= 3 && (bytes[0] & 0xff) == 0xff
                        && (bytes[1] & 0xff) == 0xd8 && (bytes[2] & 0xff) == 0xff;
                case "webp" -> bytes.length >= 12 && bytes[0] == 'R' && bytes[1] == 'I'
                        && bytes[2] == 'F' && bytes[3] == 'F' && bytes[8] == 'W'
                        && bytes[9] == 'E' && bytes[10] == 'B' && bytes[11] == 'P';
                default -> false;
            };
            if (!valid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conteúdo de imagem inválido");
            Files.createDirectories(directory);
            String filename = UUID.randomUUID() + "." + extension;
            Files.write(directory.resolve(filename), bytes);
            return publicBaseUrl + "/api/service-images/" + filename;
        } catch (IllegalArgumentException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem inválida", error);
        } catch (IOException error) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao armazenar imagem", error);
        }
    }

    public Path resolve(String filename) {
        if (!filename.matches("[0-9a-fA-F-]{36}\\.(png|jpg|webp)"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Path path = directory.resolve(filename).normalize();
        if (!path.startsWith(directory) || !Files.isRegularFile(path))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return path;
    }
}
