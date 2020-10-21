package com.fuel.consumption;

import com.fuel.consumption.api.dto.*;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.FuelType;
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

        FuelConsumptionPostRequest expectedResult = new FuelConsumptionPostRequest(FuelType.LPG.name(), new BigDecimal("1.2"), new BigDecimal("2"), LocalDate.now(), 1L);

        when(fuelConsumptionRepository.save(any())).thenReturn(FuelConsumptionPostRequest.toEntity(expectedResult));

        FuelConsumption actualResult = fuelConsumptionService.insert(expectedResult);

        Assertions.assertEquals(expectedResult.getFuelType(), actualResult.getFuelType().name());

    }

    @Test
    void bulk_insert_success_case() {

        List<FuelConsumptionPostRequest> expectedResult = new ArrayList<>();

        expectedResult.add(new FuelConsumptionPostRequest(FuelType.LPG.name(), new BigDecimal("1.2"), new BigDecimal("2"), LocalDate.now(), 1L));
        expectedResult.add(new FuelConsumptionPostRequest(FuelType.DIESEL.name(), new BigDecimal("1.2"), new BigDecimal("2"), LocalDate.now(), 1L));

        when(fuelConsumptionRepository.saveAll(any())).
                thenReturn(expectedResult
                        .stream()
                        .map(FuelConsumptionPostRequest::toEntity)
                        .collect(Collectors.toList()));

        List<FuelConsumption> actualResult = fuelConsumptionService.insertAll(expectedResult);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
    }

    @Test
    void findTotalSpentAmountOfMoneyGroupedByMonth_success_case() {

        List<TotalSpentAmountOfMoneyDto> totalSpentAmountOfMoneyDtoList = new ArrayList<>();

        totalSpentAmountOfMoneyDtoList.add(new TotalSpentAmountOfMoneyDto(10, new BigDecimal("1.12"), new BigDecimal("10.3"), 0L));
        totalSpentAmountOfMoneyDtoList.add(new TotalSpentAmountOfMoneyDto(10, new BigDecimal("1.15"), new BigDecimal("10.6"), 0L));

        List<TotalSpentAmountOfMoneyResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new TotalSpentAmountOfMoneyResponse(Month.OCTOBER.name(), new BigDecimal("23.7260")));

        when(fuelConsumptionRepository.findAllTotalSpentAmountOfMoney()).thenReturn(totalSpentAmountOfMoneyDtoList);

        List<TotalSpentAmountOfMoneyResponse> actualResult = fuelConsumptionService.findTotalSpentAmountOfMoneyGroupedByMonth();

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getMonth(), actualResult.get(0).getMonth());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());

    }

    @Test
    void findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth_success_case() {

        long driverId = 123L;
        List<TotalSpentAmountOfMoneyDto> totalSpentAmountOfMoneyDtoList = new ArrayList<>();

        totalSpentAmountOfMoneyDtoList.add(new TotalSpentAmountOfMoneyDto(10, new BigDecimal("1.12"), new BigDecimal("10.3"), driverId));
        totalSpentAmountOfMoneyDtoList.add(new TotalSpentAmountOfMoneyDto(10, new BigDecimal("1.15"), new BigDecimal("10.6"), driverId));

        List<TotalSpentAmountOfMoneyResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new TotalSpentAmountOfMoneyResponse(Month.OCTOBER.name(), new BigDecimal("23.7260")));

        when(fuelConsumptionRepository.findAllTotalSpentAmountOfMoneyByDriverId(anyLong())).thenReturn(totalSpentAmountOfMoneyDtoList);

        List<TotalSpentAmountOfMoneyResponse> actualResult = fuelConsumptionService.findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(driverId);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getMonth(), actualResult.get(0).getMonth());
        Assertions.assertEquals(expectedResult.get(0).getTotalAmount(), actualResult.get(0).getTotalAmount());
    }

    @Test
    void findFuelConsumptionRecordsByMonth_success_case() {
        int monthId = 3;

        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("20"), LocalDate.parse("2020-03-06"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("3.5"), new BigDecimal("40"), LocalDate.parse("2020-03-08"), 6L));

        when(fuelConsumptionRepository.findAll((Specification<FuelConsumption>) any())).thenReturn(fuelConsumptionList);

        List<FuelConsumptionRecordSpecifiedByMonthResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("1.5"), new BigDecimal("30.0000"), LocalDate.parse("2020-03-06"), 3L));
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.LPG.name(), new BigDecimal("40"), new BigDecimal("3.5"), new BigDecimal("140.0000"), LocalDate.parse("2020-03-08"), 6L));

        List<FuelConsumptionRecordSpecifiedByMonthResponse> actualResult = fuelConsumptionService.findFuelConsumptionRecordsByMonth(monthId);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTotalPrice(), actualResult.get(0).getTotalPrice());
        Assertions.assertEquals(expectedResult.get(1).getTotalPrice(), actualResult.get(1).getTotalPrice());
    }

    @Test
    void findFuelConsumptionRecordsByMonthAndDriverId_success_case() {
        int monthId = 2;
        long driverId = 3;

        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("1.5"), new BigDecimal("30"), LocalDate.parse("2020-02-11"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("4"), new BigDecimal("20"), LocalDate.parse("2020-02-13"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("30"), LocalDate.parse("2020-02-07"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("2.5"), new BigDecimal("20"), LocalDate.parse("2020-02-09"), 3L));


        when(fuelConsumptionRepository.findAll((Specification<FuelConsumption>) any())).thenReturn(fuelConsumptionList);

        List<FuelConsumptionRecordSpecifiedByMonthResponse> expectedResult = new ArrayList<>();
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.LPG.name(), new BigDecimal("30"), new BigDecimal("1.5"), new BigDecimal("45.0000"), LocalDate.parse("2020-03-07"), 3L));
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.LPG.name(), new BigDecimal("20"), new BigDecimal("2.5"), new BigDecimal("50.0000"), LocalDate.parse("2020-03-09"), 3L));
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.P95.name(), new BigDecimal("30"), new BigDecimal("1.5"), new BigDecimal("45.0000"), LocalDate.parse("2020-03-11"), 3L));
        expectedResult.add(new FuelConsumptionRecordSpecifiedByMonthResponse(FuelType.P95.name(), new BigDecimal("20"), new BigDecimal("4"), new BigDecimal("80.0000"), LocalDate.parse("2020-03-13"), 3L));


        List<FuelConsumptionRecordSpecifiedByMonthResponse> actualResult = fuelConsumptionService.findFuelConsumptionRecordsByMonthAndDriverId(monthId, driverId);

        Assertions.assertEquals(expectedResult.size(), actualResult.size());
        Assertions.assertEquals(expectedResult.get(0).getTotalPrice(), actualResult.get(0).getTotalPrice());
        Assertions.assertEquals(expectedResult.get(1).getTotalPrice(), actualResult.get(1).getTotalPrice());

    }

    @Test
    void findEachMonthFuelConsumptionStatisticsGroupedByFuelType_success_case() throws IOException {

        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("3.5"), new BigDecimal("30"), LocalDate.parse("2020-01-12"), 5L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("2.7"), new BigDecimal("60"), LocalDate.parse("2020-01-15"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("4"), new BigDecimal("80"), LocalDate.parse("2020-01-16"), 4L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("3"), new BigDecimal("300"), LocalDate.parse("2020-01-17"), 3L));


        when(fuelConsumptionRepository.findAll()).thenReturn(fuelConsumptionList);

        List<FuelConsumptionStatisticResponse> er = new ArrayList<>();
        List<FuelConsumptionStatisticFuelTypeResponse> fuelTypeResponseList = new ArrayList<>();
        fuelTypeResponseList.add(new FuelConsumptionStatisticFuelTypeResponse(FuelType.P95.name(), new BigDecimal("470.0000"), new BigDecimal("3.1639"), new BigDecimal("1487.0000")));
        er.add(new FuelConsumptionStatisticResponse(Month.JANUARY.name(), fuelTypeResponseList));


        List<FuelConsumptionStatisticResponse> ar = fuelConsumptionService.findEachMonthFuelConsumptionStatisticsGroupedByFuelType();

        Assertions.assertEquals(er.size(), ar.size());
        Assertions.assertEquals(er.get(0).getMonth(), ar.get(0).getMonth());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(), ar.get(0).getFuelTypeStatistics().size());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(), ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
        Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(), ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

    }

    @Test
    void findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType_success_case() throws IOException {
        long driverId = 3;
        List<FuelConsumption> fuelConsumptionList = new ArrayList<>();
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("3.5"), new BigDecimal("30"), LocalDate.parse("2020-01-12"), 5L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("2.7"), new BigDecimal("60"), LocalDate.parse("2020-01-15"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("4"), new BigDecimal("80"), LocalDate.parse("2020-01-16"), 4L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.P95, new BigDecimal("3"), new BigDecimal("300"), LocalDate.parse("2020-01-17"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("1.5"), new BigDecimal("20"), LocalDate.parse("2020-03-06"), 3L));
        fuelConsumptionList.add(new FuelConsumption(FuelType.LPG, new BigDecimal("3.5"), new BigDecimal("40"), LocalDate.parse("2020-03-08"), 6L));

        when(fuelConsumptionRepository.findByDriverId(anyLong())).thenReturn(fuelConsumptionList);

        List<FuelConsumptionStatisticResponse> er = new ArrayList<>();
        List<FuelConsumptionStatisticFuelTypeResponse> fuelTypeJanResponseList = new ArrayList<>();
        List<FuelConsumptionStatisticFuelTypeResponse> fuelTypeMarchResponseList = new ArrayList<>();
        fuelTypeJanResponseList.add(new FuelConsumptionStatisticFuelTypeResponse(FuelType.P95.name(), new BigDecimal("470.0000"), new BigDecimal("3.1639"), new BigDecimal("1487.0000")));
        fuelTypeMarchResponseList.add(new FuelConsumptionStatisticFuelTypeResponse(FuelType.LPG.name(), new BigDecimal("6.0000"), new BigDecimal("2.8334"), new BigDecimal("170.0000")));
        er.add(new FuelConsumptionStatisticResponse(Month.JANUARY.name(), fuelTypeJanResponseList));
        er.add(new FuelConsumptionStatisticResponse(Month.MARCH.name(), fuelTypeMarchResponseList));


        List<FuelConsumptionStatisticResponse> ar = fuelConsumptionService.findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(driverId);

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