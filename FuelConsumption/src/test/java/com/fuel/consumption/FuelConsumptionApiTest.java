package com.fuel.consumption;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuel.consumption.api.dto.FuelConsumptionRecordSpecifiedByMonthResponse;
import com.fuel.consumption.api.dto.FuelConsumptionStatisticFuelTypeResponse;
import com.fuel.consumption.api.dto.FuelConsumptionStatisticResponse;
import com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyResponse;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.FuelType;
import com.fuel.consumption.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@SpringBootTest
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
        String inputJson = JsonUtil.readFileAsString("src/test/resources/test-data/json_util_success_case.json");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @Test
    void registerFuelConsumptionList_success_case() throws Exception {
        String uri = "/api/v1/fuel/consumptions/list";
        String fuelConsumptionDtoList = JsonUtil.readFileAsString("src/test/resources/test-data/general_test_data.json");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(fuelConsumptionDtoList)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @Test
    void registerFuelConsumptionListWithFile_success_case() throws Exception {
        String uri = "/api/v1/fuel/consumptions/file";
        String fuelConsumptionDtoList = JsonUtil.readFileAsString("src/test/resources/test-data/general_test_data.json");
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
    void totalSpentAmountOfMoneyGroupedByMonth_success_case() throws Exception {
        String uri = "/api/v1/fuel/consumptions";

        List<TotalSpentAmountOfMoneyResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new TotalSpentAmountOfMoneyResponse(Month.JANUARY.name(), new BigDecimal("47.7900")));

        when(fuelConsumptionService.findTotalSpentAmountOfMoneyGroupedByMonth()).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<TotalSpentAmountOfMoneyResponse> actualResult = JsonUtil.getTotalSpentAmountOfMoneyResponsesString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getMonth(), actualResult.get(0).getMonth());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void totalSpentAmountOfMoneyByDriverIdAndGroupedByMonth_success_case() throws Exception {
        String uri = "/api/v1/fuel/consumptions/123";

        List<TotalSpentAmountOfMoneyResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new TotalSpentAmountOfMoneyResponse(Month.JANUARY.name(), new BigDecimal("47.7900")));

        when(fuelConsumptionService.findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(anyLong())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<TotalSpentAmountOfMoneyResponse> actualResult = JsonUtil.getTotalSpentAmountOfMoneyResponsesString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getMonth(), actualResult.get(0).getMonth());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void findFuelConsumptionRecordsByMonth_success_case() throws Exception {
        String uri = "/api/v1/fuel/consumptions/months/3";

        List<FuelConsumptionRecordSpecifiedByMonthResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("1.5"), new BigDecimal("30.0000"), LocalDate.parse("2020-03-06"), 3L));
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.LPG.name(), new BigDecimal("40"), new BigDecimal("3.5"), new BigDecimal("140.0000"), LocalDate.parse("2020-03-08"), 6L));

        when(fuelConsumptionService.findFuelConsumptionRecordsByMonth(anyInt())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<FuelConsumptionRecordSpecifiedByMonthResponse> actualResult = JsonUtil.getFuelConsumptionRecordSpecifiedByMonthsFromString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTotalPrice(), actualResult.get(0).getTotalPrice());
        Assertions.assertEquals(expectedResult.get(1).getTotalPrice(), actualResult.get(1).getTotalPrice());
    }

    @Test
    void findFuelConsumptionRecordsByMonthAndDriverId_success_case() throws Exception {

        String uri = "/api/v1/fuel/consumptions/months/2/3";

        List<FuelConsumptionRecordSpecifiedByMonthResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("1.5"), new BigDecimal("45.0000"), LocalDate.parse("2020-02-07"), 3L));
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("2.5"), new BigDecimal("50.0000"), LocalDate.parse("2020-02-09"), 3L));

        when(fuelConsumptionService.findFuelConsumptionRecordsByMonthAndDriverId(anyInt(), anyLong())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<FuelConsumptionRecordSpecifiedByMonthResponse> actualResult = JsonUtil.getFuelConsumptionRecordSpecifiedByMonthsFromString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTotalPrice(), actualResult.get(0).getTotalPrice());
        Assertions.assertEquals(expectedResult.get(1).getTotalPrice(), actualResult.get(1).getTotalPrice());
    }

    @Test
    void findEachMonthFuelConsumptionStatisticsGroupedByFuelType_success_case() throws Exception {
        String uri = "/api/v1/fuel/consumptions/statistics";

        FuelConsumptionStatisticResponse fuelConsumptionStatisticResponse = new FuelConsumptionStatisticResponse();
        fuelConsumptionStatisticResponse.setMonth(Month.AUGUST.name());
        FuelConsumptionStatisticFuelTypeResponse fuelTypeResponse = new FuelConsumptionStatisticFuelTypeResponse();
        fuelTypeResponse.setFuelType(FuelType.P95.name());
        fuelTypeResponse.setTotalPrice(new BigDecimal("1062.0000"));
        fuelTypeResponse.setAveragePrice(new BigDecimal("2.9500"));
        fuelTypeResponse.setTotalVolume(new BigDecimal("360.0000"));
        List<FuelConsumptionStatisticFuelTypeResponse> fuelTypeResponseList = new ArrayList<>();
        fuelTypeResponseList.add(fuelTypeResponse);
        fuelConsumptionStatisticResponse.setFuelTypeStatistics(fuelTypeResponseList);
        List<FuelConsumptionStatisticResponse> er = new ArrayList<>();
        er.add(fuelConsumptionStatisticResponse);

        when(fuelConsumptionService.findEachMonthFuelConsumptionStatisticsGroupedByFuelType()).thenReturn(er);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<FuelConsumptionStatisticResponse> ar = JsonUtil.getFuelConsumptionStatisticResponsesFromString(content);

        Assertions.assertEquals(er.size(), ar.size());
        Assertions.assertEquals(er.get(0).getMonth(), ar.get(0).getMonth());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(), ar.get(0).getFuelTypeStatistics().size());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(), ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(), ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

    }

    @Test
    void findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType_success_case() throws Exception {
        String uri = "/api/v1/fuel/consumptions/statistics/3";

        FuelConsumptionStatisticResponse fuelConsumptionStatisticResponse = new FuelConsumptionStatisticResponse();
        fuelConsumptionStatisticResponse.setMonth(Month.AUGUST.name());
        FuelConsumptionStatisticFuelTypeResponse fuelTypeResponse = new FuelConsumptionStatisticFuelTypeResponse();
        fuelTypeResponse.setFuelType(FuelType.P95.name());
        fuelTypeResponse.setTotalPrice(new BigDecimal("1062.0000"));
        fuelTypeResponse.setAveragePrice(new BigDecimal("2.9500"));
        fuelTypeResponse.setTotalVolume(new BigDecimal("360.0000"));
        List<FuelConsumptionStatisticFuelTypeResponse> fuelTypeResponseList = new ArrayList<>();
        fuelTypeResponseList.add(fuelTypeResponse);
        fuelConsumptionStatisticResponse.setFuelTypeStatistics(fuelTypeResponseList);
        List<FuelConsumptionStatisticResponse> er = new ArrayList<>();
        er.add(fuelConsumptionStatisticResponse);


        when(fuelConsumptionService.findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(anyLong())).thenReturn(er);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<FuelConsumptionStatisticResponse> ar = JsonUtil.getFuelConsumptionStatisticResponsesFromString(content);

        Assertions.assertEquals(er.size(), ar.size());
        Assertions.assertEquals(er.get(0).getMonth(), ar.get(0).getMonth());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(), ar.get(0).getFuelTypeStatistics().size());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(), ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(), ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

    }
}