package com.fuel.consumption;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuel.consumption.api.dto.FuelConsumptionPostRequest;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.FuelType;
import com.fuel.consumption.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@ActiveProfiles("logback-test")
@Slf4j
@AutoConfigureMockMvc
class FuelConsumptionApiTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FuelConsumptionService fuelConsumptionService;

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}
	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	void registerFuelConsumption_success_case() throws Exception {
		String uri = "/api/v1/fuel/consumptions/single";
		String inputJson= JsonUtil.readFileAsString("src/test/resources/test-data/json_util_success_case.json");

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	void registerFuelConsumptionList_success_case() throws Exception {
		String uri = "/api/v1/fuel/consumptions/list";
		String fuelConsumptionDtoList= JsonUtil.readFileAsString("src/test/resources/test-data/general_test_data.json");

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(fuelConsumptionDtoList)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	void registerFuelConsumptionListWithFile_success_case() throws Exception {
		String uri = "/api/v1/fuel/consumptions/file";
		String fuelConsumptionDtoList= JsonUtil.readFileAsString("src/test/resources/test-data/general_test_data.json");

		MockMultipartFile file
				= new MockMultipartFile(
				"file",
				"general_test_data.json",
				MediaType.TEXT_PLAIN_VALUE,
				fuelConsumptionDtoList.getBytes()
		);

		mockMvc.perform(multipart(uri).file(file)).andExpect(status().isCreated());
	}

	@Test
	void totalSpentAmountOfMoneyGroupedByMonth_success_case() {

	}

	@Test
	void totalSpentAmountOfMoneyByDriverIdAndGroupedByMonth_success_case() {

	}

	@Test
	void findFuelConsumptionRecordsByMonth_success_case() {

	}

	@Test
	void findFuelConsumptionRecordsByMonthAndDriverId_success_case() {

	}

	@Test
	void findEachMonthFuelConsumptionStatisticsGroupedByFuelType_success_case() {

	}

	@Test
	void findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType_success_case() {

	}

}