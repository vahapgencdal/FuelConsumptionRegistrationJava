package com.fuel.consumption.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@Data
public class ExpenseReportDto {

    private int month;

    private BigDecimal amount;

    @JsonIgnore
    private BigDecimal fuelPrice;

    @JsonIgnore
    private BigDecimal fuelVolume;

    @JsonIgnore
    private long driverId;

    public ExpenseReportDto(int month, BigDecimal fuelPrice, BigDecimal fuelVolume) {
        this.month = month;
        this.fuelPrice = fuelPrice;
        this.fuelVolume = fuelVolume;
    }

    public ExpenseReportDto(int month, BigDecimal fuelPrice, BigDecimal fuelVolume, long driverId) {
        this.month = month;
        this.fuelPrice = fuelPrice;
        this.fuelVolume = fuelVolume;
        this.driverId = driverId;
    }

    public BigDecimal getFuelPrice() {
        return fuelPrice.setScale(4, BigDecimal.ROUND_CEILING).stripTrailingZeros();
    }

    public BigDecimal getFuelVolume() {
        return fuelVolume.setScale(4, BigDecimal.ROUND_CEILING).stripTrailingZeros();
    }

}
