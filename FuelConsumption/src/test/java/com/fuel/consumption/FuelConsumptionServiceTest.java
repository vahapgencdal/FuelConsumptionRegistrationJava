package com.fuel.consumption;

import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@ActiveProfiles("logback-test")
@Slf4j
class FuelConsumptionServiceTest {

	@Mock
	private FuelConsumptionRepository fuelConsumptionRepository;

	@Test
	void insert_success_case() {
			//when(fuelConsumptionRepository.save(any())).thenReturn()
	}

	@Test
	void bulk_insert_success_case() {

	}

	@Test
	void findTotalSpentAmountOfMoneyGroupedByMonth_success_case(){

	}

	@Test
	void findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth_success_case(){

	}
}