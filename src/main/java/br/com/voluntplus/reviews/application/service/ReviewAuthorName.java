package br.com.voluntplus.reviews.application.service;

import br.com.voluntplus.users.api.UserSummary;

final class ReviewAuthorName {

	private ReviewAuthorName() {
	}

	static String from(UserSummary user) {
		if (user.organizationName() != null && !user.organizationName().isBlank()) {
			return user.organizationName();
		}
		return user.fullName();
	}
}
