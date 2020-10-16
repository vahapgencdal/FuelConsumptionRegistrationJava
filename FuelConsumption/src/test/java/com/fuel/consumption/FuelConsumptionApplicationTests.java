package com.fuel.consumption;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("logging-test")
@Slf4j
class FuelConsumptionApplicationTests {

	@Test
	void contextLoads() {
		log.error("asdasdasdasd");
	}

}
