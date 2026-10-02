package br.com.voluntplus;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ApplicationModulesTest {

	@Test
	void verifiesModuleBoundaries() {
		ApplicationModules.of(VoluntplusApplication.class).verify();
	}
}
