package com.fuel.consumption;

import com.fuel.consumption.api.dto.FuelConsumptionPostRequest;
import com.fuel.consumption.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@SpringBootTest
@ActiveProfiles("logback-test")
@Slf4j
public class JsonUtilTest {

    @Test
    public void read_successCase() throws IOException {
        FuelConsumptionPostRequest fuelConsumptionDtoList = JsonUtil.getJsonObjectFromFile("file:src/test/resources/test-data/json_util_success_case.json");
        Assertions.assertNotNull(fuelConsumptionDtoList);
    }

    @Test
    public void unrecognizedPropertyExceptionCase() {
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
