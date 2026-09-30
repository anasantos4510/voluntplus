package br.com.voluntplus.users.infrastructure.persistence;

import br.com.voluntplus.users.application.exception.UserAlreadyRegisteredException;
import br.com.voluntplus.users.application.port.out.UserRepository;
import br.com.voluntplus.users.domain.model.Gender;
import br.com.voluntplus.users.domain.model.PersonType;
import br.com.voluntplus.users.domain.model.User;
import br.com.voluntplus.users.domain.model.UserRole;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(
		properties = "spring.jpa.hibernate.ddl-auto=validate",
		showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
@Import(JpaUserRepositoryAdapter.class)
@Testcontainers
class JpaUserRepositoryAdapterTest {

	@Container
	private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17-alpine");

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@DynamicPropertySource
	static void configurePostgres(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRES::getUsername);
		registry.add("spring.datasource.password", POSTGRES::getPassword);
	}

	@Test
	void savesAndFindsAnIndividualById() {
		UUID userId = UUID.randomUUID();
		User user = User.createIndividual(
				userId,
				"user_individual",
				"Ana Silva",
				LocalDate.of(1995, 5, 20),
				"ana@example.com",
				Gender.FEMALE,
				UserRole.BENEFICIARY,
				LocalDate.of(2025, 1, 1));

		userRepository.save(user);
		flushAndClear();

		User persistedUser = userRepository.findById(userId).orElseThrow();

		assertEquals(userId, persistedUser.getId());
		assertEquals("user_individual", persistedUser.getClerkUserId());
		assertEquals(PersonType.INDIVIDUAL, persistedUser.getPersonType());
		assertEquals(UserRole.BENEFICIARY, persistedUser.getCurrentRole());
		assertEquals("ana@example.com", persistedUser.getEmail());
		assertEquals("Ana Silva", persistedUser.getFullName());
		assertEquals(LocalDate.of(1995, 5, 20), persistedUser.getBirthDate());
		assertEquals(Gender.FEMALE, persistedUser.getGender());
		assertNull(persistedUser.getOrganizationName());
		assertNull(persistedUser.getCnpj());
	}

	@Test
	void savesAndFindsAnOrganizationByClerkUserId() {
		User user = User.createOrganization(
				UUID.randomUUID(),
				"user_organization",
				"Instituto Voluntario",
				"contato@instituto.org",
				"11222333000181");

		userRepository.save(user);
		flushAndClear();

		User persistedUser = userRepository.findByClerkUserId("user_organization").orElseThrow();

		assertEquals(PersonType.ORGANIZATION, persistedUser.getPersonType());
		assertEquals(UserRole.OFFERER, persistedUser.getCurrentRole());
		assertEquals("Instituto Voluntario", persistedUser.getOrganizationName());
		assertEquals("contato@instituto.org", persistedUser.getEmail());
		assertEquals("11222333000181", persistedUser.getCnpj());
		assertNull(persistedUser.getFullName());
		assertNull(persistedUser.getBirthDate());
		assertNull(persistedUser.getGender());
	}

	@Test
	void rejectsTwoUsersWithTheSameClerkUserId() {
		User firstUser = User.createIndividual(
				UUID.randomUUID(),
				"user_duplicated",
				"First User",
				LocalDate.of(1990, 1, 1),
				"first@example.com",
				Gender.OTHER,
				UserRole.OFFERER,
				LocalDate.of(2025, 1, 1));
		User secondUser = User.createOrganization(
				UUID.randomUUID(),
				"user_duplicated",
				"Second Organization",
				"second@example.com",
				"44555666000190");

		userRepository.save(firstUser);

		assertThrows(UserAlreadyRegisteredException.class, () -> userRepository.save(secondUser));
	}

	@Test
	void rejectsAnOrganizationWithTheBeneficiaryRoleAtTheDatabaseBoundary() {
		String sql = """
				INSERT INTO users (
				    id, clerk_user_id, person_type, current_user_role, email, organization_name, cnpj
				) VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		assertThrows(DataIntegrityViolationException.class, () -> jdbcTemplate.update(
				sql,
				UUID.randomUUID(),
				"user_invalid_organization",
				"ORGANIZATION",
				"BENEFICIARY",
				"invalid@example.com",
				"Invalid Organization",
				"77888999000100"));
	}

	@Test
	void returnsEmptyWhenTheClerkUserIdDoesNotExist() {
		assertTrue(userRepository.findByClerkUserId("missing_user").isEmpty());
	}

	private void flushAndClear() {
		entityManager.flush();
		entityManager.clear();
	}
}
