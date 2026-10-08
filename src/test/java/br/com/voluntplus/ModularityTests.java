package br.com.voluntplus;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {

	@Test
	void verifiesModuleDependencies() {
		ApplicationModules.of(VoluntplusApplication.class).verify();
	}
}
