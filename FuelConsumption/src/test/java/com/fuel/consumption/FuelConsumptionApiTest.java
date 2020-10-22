package com.fuel.consumption;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuel.consumption.api.response.ExpenseReportResponse;
import com.fuel.consumption.api.response.FuelReportFuelTypeResponse;
import com.fuel.consumption.api.response.FuelReportMonthlyResponse;
import com.fuel.consumption.api.response.FuelReportResponse;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.FuelType;
import com.fuel.consumption.util.GroupByType;
import com.fuel.consumption.util.JsonUtil;
import com.fuel.consumption.util.ReportPeriod;
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
import static org.mockito.ArgumentMatchers.*;
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

    @Test
    void registerFuelConsumption_success_case() throws Exception {
        String uri = "/api/v1/fuel/expenses/single";
        String inputJson = JsonUtil.readFileAsString("src/test/resources/test-data/json_util_success_case.json");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @Test
    void registerFuelConsumptionList_success_case() throws Exception {
        String uri = "/api/v1/fuel/expenses/list";
        String fuelConsumptionDtoList = JsonUtil.readFileAsString("src/test/resources/test-data/general_test_data.json");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(fuelConsumptionDtoList)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @Test
    void registerFuelConsumptionListWithFile_success_case() throws Exception {
        String uri = "/api/v1/fuel/expenses/file";
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
    void findExpenseReportGroupedByMonth_success_case() throws Exception {
        String uri = "/api/v1/report/expense?period=" + ReportPeriod.MONTH;

        List<ExpenseReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new ExpenseReportResponse(Month.JANUARY.name(), new BigDecimal("47.7900")));

        when(fuelConsumptionService.findExpenseReportByPeriod(ReportPeriod.MONTH.name())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<ExpenseReportResponse> actualResult = JsonUtil.getExpenseReportString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTime(), actualResult.get(0).getTime());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void findExpenseReportGroupedByYear_success_case() throws Exception {
        String uri = "/api/v1/report/expense?period=" + ReportPeriod.YEAR;

        List<ExpenseReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new ExpenseReportResponse(String.valueOf(2020), new BigDecimal("47.7900")));

        when(fuelConsumptionService.findExpenseReportByPeriod(ReportPeriod.YEAR.name())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<ExpenseReportResponse> actualResult = JsonUtil.getExpenseReportString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTime(), actualResult.get(0).getTime());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void findExpenseReportByDriverIdAndGroupedByMonth_success_case() throws Exception {
        String uri = "/api/v1/report/expense/123?period=" + ReportPeriod.MONTH;

        List<ExpenseReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new ExpenseReportResponse(Month.JANUARY.name(), new BigDecimal("47.7900")));

        when(fuelConsumptionService.findExpenseReportByPeriodAndDriverId(anyLong(), anyString())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<ExpenseReportResponse> actualResult = JsonUtil.getExpenseReportString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTime(), actualResult.get(0).getTime());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void findExpenseReportByDriverIdAndGroupedByYear_success_case() throws Exception {
        String uri = "/api/v1/report/expense/123?period=" + ReportPeriod.YEAR;

        List<ExpenseReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new ExpenseReportResponse(String.valueOf(2020), new BigDecimal("47.7900")));

        when(fuelConsumptionService.findExpenseReportByPeriodAndDriverId(anyLong(), anyString())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<ExpenseReportResponse> actualResult = JsonUtil.getExpenseReportString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTime(), actualResult.get(0).getTime());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void fuelReportByMonthId_success_case() throws Exception {
        String uri = "/api/v1/report/fuel/3";

        List<FuelReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new FuelReportResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("1.5"), new BigDecimal("30.0000"), LocalDate.parse("2020-03-06"), 3L));
        expectedResult.add(new FuelReportResponse(FuelType.LPG.name(), new BigDecimal("40"), new BigDecimal("3.5"), new BigDecimal("140.0000"), LocalDate.parse("2020-03-08"), 6L));

        when(fuelConsumptionService.fuelReports(anyInt())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<FuelReportResponse> actualResult = JsonUtil.getFuelReportsFromString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTotalPrice(), actualResult.get(0).getTotalPrice());
        Assertions.assertEquals(expectedResult.get(1).getTotalPrice(), actualResult.get(1).getTotalPrice());
    }

    @Test
    void fuelReportByMonthIdAndDriverId_success_case() throws Exception {

        String uri = "/api/v1/report/fuel/2/3";

        List<FuelReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new FuelReportResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("1.5"), new BigDecimal("45.0000"), LocalDate.parse("2020-02-07"), 3L));
        expectedResult.add(new FuelReportResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("2.5"), new BigDecimal("50.0000"), LocalDate.parse("2020-02-09"), 3L));

        when(fuelConsumptionService.fuelReports(anyInt(), anyLong())).thenReturn(expectedResult);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<FuelReportResponse> actualResult = JsonUtil.getFuelReportsFromString(content);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTotalPrice(), actualResult.get(0).getTotalPrice());
        Assertions.assertEquals(expectedResult.get(1).getTotalPrice(), actualResult.get(1).getTotalPrice());
    }

    @Test
    void monthlyFuelReportsGroupedByFuelType_success_case() throws Exception {
        String uri = "/api/v1/report/fuel/months?groupBy=" + GroupByType.FUEL_TYPE.name();

        FuelReportMonthlyResponse fuelReportMonthlyResponse = new FuelReportMonthlyResponse();
        fuelReportMonthlyResponse.setMonth(Month.AUGUST.name());
        FuelReportFuelTypeResponse fuelTypeResponse = new FuelReportFuelTypeResponse();
        fuelTypeResponse.setFuelType(FuelType.P95.name());
        fuelTypeResponse.setTotalPrice(new BigDecimal("1062.0000"));
        fuelTypeResponse.setAveragePrice(new BigDecimal("2.9500"));
        fuelTypeResponse.setTotalVolume(new BigDecimal("360.0000"));
        List<FuelReportFuelTypeResponse> fuelTypeResponseList = new ArrayList<>();
        fuelTypeResponseList.add(fuelTypeResponse);
        fuelReportMonthlyResponse.setFuelTypeStatistics(fuelTypeResponseList);
        List<FuelReportMonthlyResponse> er = new ArrayList<>();
        er.add(fuelReportMonthlyResponse);

        when(fuelConsumptionService.monthlyFuelReports(anyString())).thenReturn(er);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<FuelReportMonthlyResponse> ar = JsonUtil.getMonthlyFuelReportsFromString(content);

        Assertions.assertEquals(er.size(), ar.size());
        Assertions.assertEquals(er.get(0).getMonth(), ar.get(0).getMonth());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(), ar.get(0).getFuelTypeStatistics().size());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(), ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(), ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

    }

    @Test
    void monthlyFuelReportsByDriverIdAndGroupedByFuelType_success_case() throws Exception {
        String uri = "/api/v1/report/fuel/months/3?groupBy=" + GroupByType.FUEL_TYPE.name();

        FuelReportMonthlyResponse fuelReportMonthlyResponse = new FuelReportMonthlyResponse();
        fuelReportMonthlyResponse.setMonth(Month.AUGUST.name());
        FuelReportFuelTypeResponse fuelTypeResponse = new FuelReportFuelTypeResponse();
        fuelTypeResponse.setFuelType(FuelType.P95.name());
        fuelTypeResponse.setTotalPrice(new BigDecimal("1062.0000"));
        fuelTypeResponse.setAveragePrice(new BigDecimal("2.9500"));
        fuelTypeResponse.setTotalVolume(new BigDecimal("360.0000"));
        List<FuelReportFuelTypeResponse> fuelTypeResponseList = new ArrayList<>();
        fuelTypeResponseList.add(fuelTypeResponse);
        fuelReportMonthlyResponse.setFuelTypeStatistics(fuelTypeResponseList);
        List<FuelReportMonthlyResponse> er = new ArrayList<>();
        er.add(fuelReportMonthlyResponse);


        when(fuelConsumptionService.monthlyFuelReports(anyLong(), anyString())).thenReturn(er);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<FuelReportMonthlyResponse> ar = JsonUtil.getMonthlyFuelReportsFromString(content);

        Assertions.assertEquals(er.size(), ar.size());
        Assertions.assertEquals(er.get(0).getMonth(), ar.get(0).getMonth());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(), ar.get(0).getFuelTypeStatistics().size());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(), ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(), ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

    }
}