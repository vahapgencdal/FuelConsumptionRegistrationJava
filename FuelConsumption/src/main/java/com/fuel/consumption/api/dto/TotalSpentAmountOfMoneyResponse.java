package com.fuel.consumption.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Total spent amount of money response")
public class TotalSpentAmountOfMoneyResponse {

    @ApiModelProperty("Month")
    private String month;

    @ApiModelProperty("Total Amount of Money")
    private BigDecimal totalAmount;


    public static TotalSpentAmountOfMoneyResponse toResponse(Map.Entry<Integer, BigDecimal> entry) {
        return new TotalSpentAmountOfMoneyResponse(Month.of(entry.getKey()).name(), entry.getValue());
    }

}
