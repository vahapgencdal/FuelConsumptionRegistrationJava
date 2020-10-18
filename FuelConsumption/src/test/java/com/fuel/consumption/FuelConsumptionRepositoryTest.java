package com.fuel.consumption;

import com.fuel.consumption.api.dto.FuelConsumptionDto;
import com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyDto;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.util.FuelType;
import com.fuel.consumption.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import sun.jvm.hotspot.utilities.Assert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@ActiveProfiles("logback-test")
@Slf4j
public class FuelConsumptionRepositoryTest {

    @Autowired
    private FuelConsumptionRepository fuelConsumptionRepository;

    public void beforeEach(String repositoryFunctionName) throws IOException {
        List<FuelConsumptionDto> fuelConsumptionDtoList= JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/"+repositoryFunctionName+".json");
        List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList.stream().map(FuelConsumptionDto::toEntity).collect(Collectors.toList());
        fuelConsumptionRepository.saveAll(fuelConsumptionList);
    }

    @Test
    public void findAllTotalSpentAmountOfMoney_Success_Case() throws IOException {
        beforeEach("findAllTotalSpentAmountOfMoney");
        List<TotalSpentAmountOfMoneyDto> totalSpentAmountOfMoneyDtoList = fuelConsumptionRepository.findAllTotalSpentAmountOfMoney();
        Assertions.assertEquals(1, totalSpentAmountOfMoneyDtoList.size());
        Assertions.assertEquals(new BigDecimal("1.12").setScale(4,BigDecimal.ROUND_CEILING).stripTrailingZeros(), totalSpentAmountOfMoneyDtoList.get(0).getFuelPrice());
        Assertions.assertEquals(new BigDecimal("40.4").setScale(4,BigDecimal.ROUND_CEILING).stripTrailingZeros(), totalSpentAmountOfMoneyDtoList.get(0).getFuelVolume());
        Assertions.assertEquals(12, totalSpentAmountOfMoneyDtoList.get(0).getMonth());
    }

    @Test
    public void findCategoriesWithConsultantCountByDriverId_Success_case() throws IOException {
        beforeEach("findAllTotalSpentAmountOfMoneyByDriverId");
        List<TotalSpentAmountOfMoneyDto> totalSpentAmountOfMoneyDtoList = fuelConsumptionRepository.findAllTotalSpentAmountOfMoneyByDriverId(123L);
        Assertions.assertEquals(1, totalSpentAmountOfMoneyDtoList.size());
        Assertions.assertEquals(new BigDecimal("1.12").setScale(4,BigDecimal.ROUND_CEILING).stripTrailingZeros(), totalSpentAmountOfMoneyDtoList.get(0).getFuelPrice());
        Assertions.assertEquals(new BigDecimal("40.4").setScale(4,BigDecimal.ROUND_CEILING).stripTrailingZeros(), totalSpentAmountOfMoneyDtoList.get(0).getFuelVolume());
        Assertions.assertEquals(123L, totalSpentAmountOfMoneyDtoList.get(0).getDriverId());
        Assertions.assertEquals(12, totalSpentAmountOfMoneyDtoList.get(0).getMonth());
    }

}
