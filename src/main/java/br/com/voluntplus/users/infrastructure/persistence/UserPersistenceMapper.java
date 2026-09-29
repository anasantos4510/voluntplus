package br.com.voluntplus.users.infrastructure.persistence;

import br.com.voluntplus.users.domain.model.User;

final class UserPersistenceMapper {

	private UserPersistenceMapper() {
	}

	static UserJpaEntity toEntity(User user) {
		return new UserJpaEntity(
				user.getId(),
				user.getClerkUserId(),
				user.getPersonType(),
				user.getCurrentRole(),
				user.getEmail(),
				user.getFullName(),
				user.getBirthDate(),
				user.getGender(),
				user.getOrganizationName(),
				user.getCnpj());
	}

	static User toDomain(UserJpaEntity entity) {
		return User.restore(
				entity.getId(),
				entity.getClerkUserId(),
				entity.getPersonType(),
				entity.getCurrentRole(),
				entity.getEmail(),
				entity.getFullName(),
				entity.getBirthDate(),
				entity.getGender(),
				entity.getOrganizationName(),
				entity.getCnpj());
	}
}
