package br.com.voluntplus.volunteerservices.application.port.out;

public interface ImageStorage {
    /** Persists uploaded data URLs; existing remote URLs pass through unchanged. */
    String storeIfUploaded(String image);
}
