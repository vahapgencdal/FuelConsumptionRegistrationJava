package com.fuel.consumption.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fuel.consumption.api.request.FuelReportPostRequest;
import com.fuel.consumption.api.response.FuelReportResponse;
import com.fuel.consumption.api.response.FuelReportMonthlyResponse;
import com.fuel.consumption.api.response.ExpenseReportResponse;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonUtil {

    private JsonUtil() {
    }

    public static List<FuelReportPostRequest> getJsonListFromFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(new URL(path), new TypeReference<List<FuelReportPostRequest>>() {
        });
    }

    public static FuelReportPostRequest getJsonObjectFromFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(new URL(path), FuelReportPostRequest.class);
    }

    public static String readFileAsString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public static List<FuelReportPostRequest> getJsonListFromString(String content) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(content, new TypeReference<List<FuelReportPostRequest>>() {
        });

    }

    public static List<ExpenseReportResponse> getExpenseReportString(String content) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(content, new TypeReference<List<ExpenseReportResponse>>() {
        });

    }

    public static List<FuelReportResponse> getFuelReportsFromString(String content) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(content, new TypeReference<List<FuelReportResponse>>() {
        });

    }

    public static List<FuelReportMonthlyResponse> getMonthlyFuelReportsFromString(String content) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(content, new TypeReference<List<FuelReportMonthlyResponse>>() {
        });

    }


}
