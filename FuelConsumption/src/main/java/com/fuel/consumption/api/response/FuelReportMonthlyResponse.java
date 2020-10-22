package com.fuel.consumption.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.List;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@ApiModel(description = "Fuel consumption statistic response for each month")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelReportMonthlyResponse {

    @ApiModelProperty(notes = "Fuel consumption statistic for each month property")
    private String month;


    @ApiModelProperty(notes = "Fuel consumption statistic group by Fuel Type mapping response List ")
    private List<FuelReportFuelTypeResponse> fuelTypeStatistics;

    public static FuelReportMonthlyResponse toResponse(int month, List<FuelReportFuelTypeResponse> fuelTypeResponses) {
        return new FuelReportMonthlyResponse(Month.of(month).name(), fuelTypeResponses);
    }

}
