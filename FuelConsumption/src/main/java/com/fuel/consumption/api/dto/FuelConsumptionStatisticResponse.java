package com.fuel.consumption.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Month;
import java.util.List;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@ApiModel(description="Fuel consumption statistic response for each month")
@Data
@AllArgsConstructor
public class FuelConsumptionStatisticResponse {

    @ApiModelProperty(notes="Fuel consumption statistic for each month property")
    private String month;


    @ApiModelProperty(notes="Fuel consumption statistic group by Fuel Type mapping response List ")
    private List<FuelConsumptionStatisticFuelTypeResponse> fuelTypeStatistics;

    public static FuelConsumptionStatisticResponse toResponse(int month, List<FuelConsumptionStatisticFuelTypeResponse> fuelTypeResponses){
        return new FuelConsumptionStatisticResponse(Month.of(month).name(),fuelTypeResponses);
    }

}
