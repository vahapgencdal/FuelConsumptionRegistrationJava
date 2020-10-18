package com.fuel.consumption;

import com.fuel.consumption.api.dto.FuelConsumptionDto;
import com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyDto;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
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


@SpringBootTest
@ActiveProfiles("logback-test")
@TestPropertySource(locations="classpath:test.properties")
@Slf4j
public class JsonUtilTest {

    @Test
    public void read_successCase() throws IOException {
        List<FuelConsumptionDto> fuelConsumptionDtoList= JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/json_util_success_case.json");
        Assertions.assertEquals(1,fuelConsumptionDtoList.size());
    }

    @Test
    public void unrecognizedPropertyExceptionCase(){
        Assertions.assertThrows(com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class, () -> {
            JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/json_util_wrong_case.json");
        });
    }

    @Test
    public void IOExceptionCase() {
        Assertions.assertThrows(IOException.class, () -> {
            JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/wrong_path.json");
        });
    }

    @Test
    public void invalidFormatExceptionCase() {

        Assertions.assertThrows(com.fasterxml.jackson.databind.exc.InvalidFormatException.class, () -> {
            JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/json_util_wrong_date_case.json");
        });
    }

}
