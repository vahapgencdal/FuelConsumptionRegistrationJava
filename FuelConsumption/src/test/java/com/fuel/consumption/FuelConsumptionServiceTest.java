package com.fuel.consumption;

import com.fuel.consumption.api.request.FuelReportPostRequest;
import com.fuel.consumption.api.response.*;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.FuelType;
import com.fuel.consumption.util.GroupByType;
import com.fuel.consumption.util.ReportPeriod;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@SpringBootTest
@ActiveProfiles("logback-test")
@Slf4j
class FuelConsumptionServiceTest {

    @MockBean
    private FuelConsumptionRepository fuelConsumptionRepository;

    @Autowired
    private FuelConsumptionService fuelConsumptionService;

    @Test
    void insert_success_case() {

        FuelReportPostRequest expectedResult = new FuelReportPostRequest(FuelType.LPG.name(), new BigDecimal("1.2"), new BigDecimal("2"), LocalDate.now(), 1L);

        when(fuelConsumptionRepository.save(any())).thenReturn(FuelReportPostRequest.toEntity(expectedResult));

        FuelConsumption actualResult = fuelConsumptionService.insert(expectedResult);

        Assertions.assertEquals(expectedResult.getFuelType(), actualResult.getFuelType().name());

    }

    @Test
    void bulk_insert_success_case() {

        List<FuelReportPostRequest> expectedResult = new ArrayList<>();

        expectedResult.add(new FuelReportPostRequest(FuelType.LPG.name(), new BigDecimal("1.2"), new BigDecimal("2"), LocalDate.now(), 1L));
        expectedResult.add(new FuelReportPostRequest(FuelType.DIESEL.name(), new BigDecimal("1.2"), new BigDecimal("2"), LocalDate.now(), 1L));

        when(fuelConsumptionRepository.saveAll(any())).
                thenReturn(expectedResult
                        .stream()
                        .map(FuelReportPostRequest::toEntity)
                        .collect(Collectors.toList()));

        List<FuelConsumption> actualResult = fuelConsumptionService.insertAll(expectedResult);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
    }

    @Test
    void findExpenseReportGroupedByMonth_success_case() {

        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();

        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("20"), LocalDate.parse("2020-03-06"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.DIESEL, new BigDecimal("3.5"), new BigDecimal("40"), LocalDate.parse("2020-03-08"), 6L));

        List<ExpenseReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new ExpenseReportResponse(Month.MARCH.name(), new BigDecimal("170.0000")));

        when(fuelConsumptionRepository.findAll()).thenReturn(fuelConsumptionList);

        List<ExpenseReportResponse> actualResult = fuelConsumptionService.findExpenseReportByPeriod(ReportPeriod.MONTH.name());

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTime(), actualResult.get(0).getTime());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void findExpenseReportGroupedByYear_success_case() {

        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();

        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("20"), LocalDate.parse("2020-03-06"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.DIESEL, new BigDecimal("3.5"), new BigDecimal("40"), LocalDate.parse("2020-03-08"), 6L));

        List<ExpenseReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new ExpenseReportResponse(String.valueOf(2020), new BigDecimal("170.0000")));

        when(fuelConsumptionRepository.findAll()).thenReturn(fuelConsumptionList);

        List<ExpenseReportResponse> actualResult = fuelConsumptionService.findExpenseReportByPeriod(ReportPeriod.YEAR.name());

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTime(), actualResult.get(0).getTime());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void findExpenseReportByDriverIdAndGroupedByMonth_success_case() {

        long driverId = 3L;
        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();

        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("20"), LocalDate.parse("2020-03-06"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.DIESEL, new BigDecimal("3.5"), new BigDecimal("40"), LocalDate.parse("2020-03-08"), 3L));

        List<ExpenseReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new ExpenseReportResponse(Month.MARCH.name(), new BigDecimal("170.0000")));

        when(fuelConsumptionRepository.findByDriverId(anyLong())).thenReturn(fuelConsumptionList);

        List<ExpenseReportResponse> actualResult = fuelConsumptionService.findExpenseReportByPeriodAndDriverId(driverId, ReportPeriod.MONTH.name());

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTime(), actualResult.get(0).getTime());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());
    }

    @Test
    void findExpenseReportByDriverIdAndGroupedByYear_success_case() {

        long driverId = 3L;
        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();

        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("20"), LocalDate.parse("2020-03-06"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.DIESEL, new BigDecimal("3.5"), new BigDecimal("40"), LocalDate.parse("2020-03-08"), 3L));

        List<ExpenseReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new ExpenseReportResponse(String.valueOf(2020), new BigDecimal("170.0000")));

        when(fuelConsumptionRepository.findByDriverId(anyLong())).thenReturn(fuelConsumptionList);

        List<ExpenseReportResponse> actualResult = fuelConsumptionService.findExpenseReportByPeriodAndDriverId(driverId, ReportPeriod.YEAR.name());

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTime(), actualResult.get(0).getTime());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());
    }


    @Test
    void fuelReportByMonthId_success_case() {
        int monthId = 3;

        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("20"), LocalDate.parse("2020-03-06"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("3.5"), new BigDecimal("40"), LocalDate.parse("2020-03-08"), 6L));

        when(fuelConsumptionRepository.findAll((Specification<FuelConsumption>) any())).thenReturn(fuelConsumptionList);

        List<FuelReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new FuelReportResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("1.5"), new BigDecimal("30.0000"), LocalDate.parse("2020-03-06"), 3L));
        expectedResult.add(new FuelReportResponse(FuelType.LPG.name(), new BigDecimal("40"), new BigDecimal("3.5"), new BigDecimal("140.0000"), LocalDate.parse("2020-03-08"), 6L));

        List<FuelReportResponse> actualResult = fuelConsumptionService.fuelReports(monthId);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTotalPrice(), actualResult.get(0).getTotalPrice());
        Assertions.assertEquals(expectedResult.get(1).getTotalPrice(), actualResult.get(1).getTotalPrice());
    }

    @Test
    void fuelReportByMonthIdAndDriverId_success_case() {
        int monthId = 2;
        long driverId = 3;

        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("1.5"), new BigDecimal("30"), LocalDate.parse("2020-02-11"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("4"), new BigDecimal("20"), LocalDate.parse("2020-02-13"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("30"), LocalDate.parse("2020-02-07"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("2.5"), new BigDecimal("20"), LocalDate.parse("2020-02-09"), 3L));


        when(fuelConsumptionRepository.findAll((Specification<FuelConsumption>) any())).thenReturn(fuelConsumptionList);

        List<FuelReportResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new FuelReportResponse(FuelType.LPG.name(), new BigDecimal("30"), new BigDecimal("1.5"), new BigDecimal("45.0000"), LocalDate.parse("2020-03-07"), 3L));
        expectedResult.add(new FuelReportResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("2.5"), new BigDecimal("50.0000"), LocalDate.parse("2020-03-09"), 3L));
        expectedResult.add(new FuelReportResponse(FuelType.P95.name(), new BigDecimal("30"), new BigDecimal("1.5"), new BigDecimal("45.0000"), LocalDate.parse("2020-03-11"), 3L));
        expectedResult.add(new FuelReportResponse(FuelType.P95.name(), new BigDecimal("20"), new BigDecimal("4"), new BigDecimal("80.0000"), LocalDate.parse("2020-03-13"), 3L));


        List<FuelReportResponse> actualResult = fuelConsumptionService.fuelReports(monthId, driverId);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTotalPrice(), actualResult.get(0).getTotalPrice());
        Assertions.assertEquals(expectedResult.get(1).getTotalPrice(), actualResult.get(1).getTotalPrice());

    }

    @Test
    void monthlyFuelReports_success_case() throws IOException {

        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("3.5"), new BigDecimal("30"), LocalDate.parse("2020-01-12"), 5L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("2.7"), new BigDecimal("60"), LocalDate.parse("2020-01-15"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("4"), new BigDecimal("80"), LocalDate.parse("2020-01-16"), 4L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("3"), new BigDecimal("300"), LocalDate.parse("2020-01-17"), 3L));


        when(fuelConsumptionRepository.findAll()).thenReturn(fuelConsumptionList);

        List<FuelReportMonthlyResponse> er = new ArrayList<>();
        List<FuelReportFuelTypeResponse> fuelTypeResponseList = new ArrayList<>();
        fuelTypeResponseList.add(new FuelReportFuelTypeResponse(FuelType.P95.name(), new BigDecimal("470.0000"), new BigDecimal("3.1639"), new BigDecimal("1487.0000")));
        er.add(new FuelReportMonthlyResponse(Month.JANUARY.name(), fuelTypeResponseList));


        List<FuelReportMonthlyResponse> ar = fuelConsumptionService.monthlyFuelReports(GroupByType.FUEL_TYPE.name());

        Assertions.assertEquals(er.size(), ar.size());
        Assertions.assertEquals(er.get(0).getMonth(), ar.get(0).getMonth());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(), ar.get(0).getFuelTypeStatistics().size());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(), ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(), ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

    }

    @Test
    void monthlyFuelReportsByDriverId_success_case() throws IOException {
        long driverId = 3;
        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("3.5"), new BigDecimal("30"), LocalDate.parse("2020-01-12"), 5L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("2.7"), new BigDecimal("60"), LocalDate.parse("2020-01-15"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("4"), new BigDecimal("80"), LocalDate.parse("2020-01-16"), 4L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("3"), new BigDecimal("300"), LocalDate.parse("2020-01-17"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("20"), LocalDate.parse("2020-03-06"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("3.5"), new BigDecimal("40"), LocalDate.parse("2020-03-08"), 6L));

        when(fuelConsumptionRepository.findByDriverId(anyLong())).thenReturn(fuelConsumptionList);

        List<FuelReportMonthlyResponse> er = new ArrayList<>();
        List<FuelReportFuelTypeResponse> fuelTypeJanResponseList = new ArrayList<>();
        List<FuelReportFuelTypeResponse> fuelTypeMarchResponseList = new ArrayList<>();
        fuelTypeJanResponseList.add(new FuelReportFuelTypeResponse(FuelType.P95.name(), new BigDecimal("470.0000"), new BigDecimal("3.1639"), new BigDecimal("1487.0000")));
        fuelTypeMarchResponseList.add(new FuelReportFuelTypeResponse(FuelType.LPG.name(), new BigDecimal("6.0000"), new BigDecimal("2.8334"), new BigDecimal("170.0000")));
        er.add(new FuelReportMonthlyResponse(Month.JANUARY.name(), fuelTypeJanResponseList));
        er.add(new FuelReportMonthlyResponse(Month.MARCH.name(), fuelTypeMarchResponseList));


        List<FuelReportMonthlyResponse> ar = fuelConsumptionService.monthlyFuelReports(driverId, GroupByType.FUEL_TYPE.name());

        Assertions.assertEquals(er.size(), ar.size());
        Assertions.assertEquals(er.get(0).getMonth(), ar.get(0).getMonth());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(), ar.get(0).getFuelTypeStatistics().size());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(), ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(), ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

        Assertions.assertEquals(er.size(), ar.size());
        Assertions.assertEquals(er.get(1).getMonth(), ar.get(1).getMonth());
        Assertions.assertEquals(er.get(1).getFuelTypeStatistics().size(), ar.get(1).getFuelTypeStatistics().size());
        Assertions.assertEquals(er.get(1).getFuelTypeStatistics().get(0).getAveragePrice(), ar.get(1).getFuelTypeStatistics().get(0).getAveragePrice());
        Assertions.assertEquals(er.get(1).getFuelTypeStatistics().get(0).getTotalPrice(), ar.get(1).getFuelTypeStatistics().get(0).getTotalPrice());

    }
}