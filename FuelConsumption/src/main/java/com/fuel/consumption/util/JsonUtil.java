package com.fuel.consumption.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fuel.consumption.api.dto.FuelConsumptionDto;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class JsonUtil {

    private JsonUtil() {
    }

    public static List<FuelConsumptionDto> getJsonListFromFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(new URL(path), new TypeReference<List<FuelConsumptionDto>>(){});
    }

}
