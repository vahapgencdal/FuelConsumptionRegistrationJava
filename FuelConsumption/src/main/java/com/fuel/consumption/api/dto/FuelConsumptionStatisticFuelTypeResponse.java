package com.fuel.consumption.api.dto;

import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.util.BigDecimalUtil;
import com.fuel.consumption.util.FuelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@Data
@ApiModel(description="This response created after Fuel Type mapping")
public class FuelConsumptionStatisticFuelTypeResponse {

    @ApiModelProperty(notes="Fuel type information added to records")
    private String fuelType;

    @ApiModelProperty(notes="after fuel Type mapping then we are using total volume for average. because of that we are showing total volume other way we need show list of volumes")
    private BigDecimal totalVolume;

    @ApiModelProperty(notes="Average price of Grouping by Fuel Type for one month: (totalPrice)/total volume")
    private BigDecimal averagePrice;

    @ApiModelProperty(notes="Total Price of Grouping by Fuel Type for one month: (aggregation(price*volume) each record)")
    private BigDecimal totalPrice;

    public static FuelConsumptionStatisticFuelTypeResponse toResponse(Map.Entry<FuelType,List<FuelConsumption>> fuelTypeListMapEntry){
        FuelConsumptionStatisticFuelTypeResponse dto = new FuelConsumptionStatisticFuelTypeResponse();
        dto.setAveragePrice(getAveragePrice(fuelTypeListMapEntry.getValue()));
        dto.setFuelType(fuelTypeListMapEntry.getKey().name());
        dto.setTotalPrice(getTotalPrice(fuelTypeListMapEntry.getValue()));
        dto.setTotalVolume(getTotalVolume(fuelTypeListMapEntry.getValue()));
        return dto;
    }

    public static BigDecimal getTotalVolume(List<FuelConsumption> consumptions){
        return consumptions.stream().map(FuelConsumption::getFuelVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(BigDecimalUtil.SCALE,BigDecimalUtil.ROUNDING_MODE);
    }

    public static BigDecimal getTotalPrice(List<FuelConsumption> consumptions){
        return consumptions.stream().map(x -> x.getFuelPrice().multiply(x.getFuelVolume()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(BigDecimalUtil.SCALE,BigDecimalUtil.ROUNDING_MODE);
    }

    public static BigDecimal getAveragePrice(List<FuelConsumption> consumptions){
        BigDecimal sumOfFuelVolume=consumptions
                .stream()
                .map(FuelConsumption::getFuelVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalConsumption = consumptions.stream().map(x -> x.getFuelPrice().multiply(x.getFuelVolume()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalConsumption.divide(sumOfFuelVolume,BigDecimalUtil.SCALE,BigDecimalUtil.ROUNDING_MODE);
    }
}
