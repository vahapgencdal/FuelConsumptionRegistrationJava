package com.fuel.consumption.api.response;

import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.util.BigDecimalUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Fuel consumption record specified by month response")
public class FuelReportResponse {

    @ApiModelProperty(notes = "Fuel Type")
    private String fuelType;

    @ApiModelProperty(notes = "Fuel Volume")
    private BigDecimal fuelVolume;

    @ApiModelProperty(notes = "Fuel Price")
    private BigDecimal fuelPrice;

    @ApiModelProperty(notes = "Total Price; Fuel Price * Fuel Volume")
    private BigDecimal totalPrice;

    @ApiModelProperty(notes = "Fuel Consumption Date")
    private LocalDate consumptionDate;

    @ApiModelProperty(notes = "Driver Id")
    private long driverId;


    public static FuelReportResponse toDto(FuelConsumption entity) {
        FuelReportResponse dto = new FuelReportResponse();
        dto.setFuelType(entity.getFuelType().name());
        dto.setFuelVolume(entity.getFuelVolume());
        dto.setFuelPrice(entity.getFuelPrice());
        dto.setTotalPrice(entity.getFuelPrice().multiply(entity.getFuelVolume())
                .setScale(BigDecimalUtil.SCALE, BigDecimalUtil.ROUNDING_MODE));
        dto.setConsumptionDate(entity.getConsumptionDate());
        dto.setDriverId(entity.getDriverId());
        return dto;
    }

}
