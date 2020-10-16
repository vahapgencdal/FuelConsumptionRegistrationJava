package com.fuel.consumption.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FuelConsumptionMonthlyStatisticDto {

    private String fuelType;
    private BigDecimal volume;
    private BigDecimal averagePrice;
    private BigDecimal totalPrice;

}
