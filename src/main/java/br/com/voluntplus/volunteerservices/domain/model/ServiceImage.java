package br.com.voluntplus.volunteerservices.domain.model;

import br.com.voluntplus.volunteerservices.domain.exception.InvalidServiceImageException;

import java.util.Base64;
import java.util.Set;

public final class ServiceImage {

	private static final int MAXIMUM_SIZE_IN_BYTES = 5 * 1024 * 1024;
	private static final int MAXIMUM_BASE64_LENGTH = 4 * ((MAXIMUM_SIZE_IN_BYTES + 2) / 3);
	private static final Set<String> ACCEPTED_MEDIA_TYPES = Set.of("image/png", "image/jpeg", "image/webp");

	private final String dataUrl;

	public ServiceImage(String dataUrl) {
		if (dataUrl == null || dataUrl.isBlank()) {
			throw new InvalidServiceImageException("Service image must not be blank");
		}

		int separatorIndex = dataUrl.indexOf(',');
		if (separatorIndex < 0) {
			throw new InvalidServiceImageException("Service image must be a valid Data URL");
		}

		String metadata = dataUrl.substring(0, separatorIndex);
		String encodedContent = dataUrl.substring(separatorIndex + 1);
		String mediaType = metadata.startsWith("data:") && metadata.endsWith(";base64")
				? metadata.substring(5, metadata.length() - 7)
				: null;

		if (!ACCEPTED_MEDIA_TYPES.contains(mediaType)) {
			throw new InvalidServiceImageException("Unsupported service image media type");
		}
		if (encodedContent.length() > MAXIMUM_BASE64_LENGTH) {
			throw new InvalidServiceImageException("Service image must not exceed 5 MB");
		}

		byte[] decodedContent;
		try {
			decodedContent = Base64.getDecoder().decode(encodedContent);
		} catch (IllegalArgumentException exception) {
			throw new InvalidServiceImageException("Service image contains invalid Base64", exception);
		}

		if (decodedContent.length == 0) {
			throw new InvalidServiceImageException("Service image content must not be empty");
		}
		if (decodedContent.length > MAXIMUM_SIZE_IN_BYTES) {
			throw new InvalidServiceImageException("Service image must not exceed 5 MB");
		}
		if (!hasExpectedSignature(mediaType, decodedContent)) {
			throw new InvalidServiceImageException("Service image content does not match its media type");
		}

		this.dataUrl = dataUrl;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	private static boolean hasExpectedSignature(String mediaType, byte[] content) {
		return switch (mediaType) {
			case "image/png" -> startsWith(content, new int[] { 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A });
			case "image/jpeg" -> startsWith(content, new int[] { 0xFF, 0xD8, 0xFF });
			case "image/webp" -> content.length >= 12
					&& matchesAt(content, 0, "RIFF")
					&& matchesAt(content, 8, "WEBP");
			default -> false;
		};
	}

	private static boolean startsWith(byte[] content, int[] signature) {
		if (content.length < signature.length) {
			return false;
		}
		for (int index = 0; index < signature.length; index++) {
			if (Byte.toUnsignedInt(content[index]) != signature[index]) {
				return false;
			}
		}
		return true;
	}

	private static boolean matchesAt(byte[] content, int offset, String signature) {
		for (int index = 0; index < signature.length(); index++) {
			if (content[offset + index] != (byte) signature.charAt(index)) {
				return false;
			}
		}
		return true;
	}
}
