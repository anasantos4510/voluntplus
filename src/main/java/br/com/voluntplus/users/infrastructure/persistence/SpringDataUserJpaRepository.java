package br.com.voluntplus.users.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface SpringDataUserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

	Optional<UserJpaEntity> findByClerkUserId(String clerkUserId);
}
