package com.fuel.consumption;

import com.fuel.consumption.api.request.FuelReportPostRequest;
import com.fuel.consumption.api.dto.ExpenseReportDto;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.util.BigDecimalUtil;
import com.fuel.consumption.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@ActiveProfiles("logback-test")
@Slf4j
public class FuelConsumptionRepositoryTest {

    @Autowired
    private FuelConsumptionRepository fuelConsumptionRepository;

    public void readJson(String repositoryFunctionName) throws IOException {

        List<FuelReportPostRequest> fuelConsumptionDtoList = JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/" + repositoryFunctionName + ".json");

        List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList.stream().map(FuelReportPostRequest::toEntity).collect(Collectors.toList());

        fuelConsumptionRepository.saveAll(fuelConsumptionList);
    }

    @AfterEach
    public void afterEach() {
        fuelConsumptionRepository.deleteAll();
    }

    @Test
    public void findAllTotalSpentAmountOfMoney_Success_Case() throws IOException {

        readJson("find_all_total_spent_amount_of_money");

        List<ExpenseReportDto> expenseReportDtoList = fuelConsumptionRepository.findExpenseReportByPeriod();

        Assertions.assertEquals(1, expenseReportDtoList.size());
        Assertions.assertEquals(10, expenseReportDtoList.get(0).getMonth());
    }

    @Test
    public void findCategoriesWithConsultantCountByDriverId_Success_case() throws IOException {

        readJson("find_all_total_spent_amount_of_money_by_driver");

        List<ExpenseReportDto> expenseReportDtoList = fuelConsumptionRepository.findExpenseReportByPeriodByDriverId(123L);

        Assertions.assertEquals(1, expenseReportDtoList.size());
        Assertions.assertEquals(new BigDecimal("1.12").setScale(4, BigDecimal.ROUND_CEILING).stripTrailingZeros(), expenseReportDtoList.get(0).getFuelPrice());
        Assertions.assertEquals(new BigDecimal("40.4").setScale(4, BigDecimal.ROUND_CEILING).stripTrailingZeros(), expenseReportDtoList.get(0).getFuelVolume());
        Assertions.assertEquals(123L, expenseReportDtoList.get(0).getDriverId());
        Assertions.assertEquals(12, expenseReportDtoList.get(0).getMonth());
    }

    @Test
    public void findByDriverId_Success_case() throws IOException {

        readJson("find_by_driver_id_repo_data");

        List<FuelConsumption> fuelConsumptionList = fuelConsumptionRepository.findByDriverId(123L);

        Assertions.assertEquals(1, fuelConsumptionList.size());
        Assertions.assertEquals(new BigDecimal("1.12").setScale(BigDecimalUtil.SCALE, BigDecimalUtil.ROUNDING_MODE).stripTrailingZeros(), fuelConsumptionList.get(0).getFuelPrice().setScale(BigDecimalUtil.SCALE, BigDecimalUtil.ROUNDING_MODE).stripTrailingZeros());
        Assertions.assertEquals(new BigDecimal("40.40").setScale(BigDecimalUtil.SCALE, BigDecimalUtil.ROUNDING_MODE).stripTrailingZeros(), fuelConsumptionList.get(0).getFuelVolume().setScale(BigDecimalUtil.SCALE, BigDecimalUtil.ROUNDING_MODE).stripTrailingZeros());
        Assertions.assertEquals(123L, fuelConsumptionList.get(0).getDriverId());
        Assertions.assertEquals(12, fuelConsumptionList.get(0).getConsumptionDate().getMonth().getValue());
    }


}
