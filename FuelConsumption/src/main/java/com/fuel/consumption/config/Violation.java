package com.fuel.consumption.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
public class Violation {

    private final String fieldName;
    private final String message;
}
