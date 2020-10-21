package com.fuel.consumption.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fuel.consumption.api.dto.FuelConsumptionPostRequest;
import com.fuel.consumption.api.dto.FuelConsumptionRecordSpecifiedByMonthDto;
import com.fuel.consumption.api.dto.FuelConsumptionStatisticResponse;
import com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyResponse;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonUtil {

    private JsonUtil() {
    }

    public static List<FuelConsumptionPostRequest> getJsonListFromFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(new URL(path), new TypeReference<List<FuelConsumptionPostRequest>>(){});
    }

    public static FuelConsumptionPostRequest getJsonObjectFromFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(new URL(path), FuelConsumptionPostRequest.class);
    }

    public static String readFileAsString(String path)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public static List<FuelConsumptionPostRequest> getJsonListFromString(String content) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(content, new TypeReference<List<FuelConsumptionPostRequest>>(){});

    }

    public static List<TotalSpentAmountOfMoneyResponse> getTotalSpentAmountOfMoneyResponsesString(String content) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(content, new TypeReference<List<TotalSpentAmountOfMoneyResponse>>(){});

    }

    public static List<FuelConsumptionRecordSpecifiedByMonthDto> getFuelConsumptionRecordSpecifiedByMonthsFromString(String content) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(content, new TypeReference<List<FuelConsumptionRecordSpecifiedByMonthDto>>(){});

    }

    public static List<FuelConsumptionStatisticResponse> getFuelConsumptionStatisticResponsesFromString(String content) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(content, new TypeReference<List<FuelConsumptionStatisticResponse>>(){});

    }



}
