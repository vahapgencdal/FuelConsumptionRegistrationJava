package com.fuel.consumption.api.response;


import com.fuel.consumption.util.ReportPeriod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Map;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@Data
@ApiModel(description = "Total spent amount of money response")
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseReportResponse {

    @ApiModelProperty("Time; it can be MONTH or YEAR")
    private String time;

    @ApiModelProperty("Total Amount of Money")
    protected BigDecimal totalAmount;

    public static ExpenseReportResponse toResponse(Map.Entry<Integer, BigDecimal> entry, String period) {
        return new ExpenseReportResponse(ReportPeriod.MONTH.name().equals(period) ? Month.of(entry.getKey()).name() : String.valueOf(entry.getKey()), entry.getValue());
    }

}
