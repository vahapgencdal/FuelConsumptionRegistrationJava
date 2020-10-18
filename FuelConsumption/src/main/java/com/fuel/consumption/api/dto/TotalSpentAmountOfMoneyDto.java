package com.fuel.consumption.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(description="Total Spent Amount of Money Grouped By Month Service Data Transaction Object. ")
@Data
public class TotalSpentAmountOfMoneyDto {

    @ApiModelProperty(notes="The Month of Year")
    @JsonProperty(value = "month")
    private int month;

    @ApiModelProperty(notes="Total spent amount of money")
    @JsonProperty(value = "spent_amount")
    private BigDecimal amount;

    @JsonIgnore
    private BigDecimal fuelPrice;

    @JsonIgnore
    private BigDecimal fuelVolume;

    @JsonIgnore
    private long driverId;

    public TotalSpentAmountOfMoneyDto(int month, BigDecimal fuelPrice, BigDecimal fuelVolume) {
        this.month = month;
        this.fuelPrice = fuelPrice;
        this.fuelVolume = fuelVolume;
    }

    public TotalSpentAmountOfMoneyDto(int month, BigDecimal fuelPrice, BigDecimal fuelVolume, long driverId) {
        this.month = month;
        this.fuelPrice = fuelPrice;
        this.fuelVolume = fuelVolume;
        this.driverId = driverId;
    }

    public BigDecimal getFuelPrice() {
        return fuelPrice.setScale(4,BigDecimal.ROUND_CEILING).stripTrailingZeros();
    }

    public BigDecimal getFuelVolume() {
        return fuelVolume.setScale(4,BigDecimal.ROUND_CEILING).stripTrailingZeros();
    }
}
