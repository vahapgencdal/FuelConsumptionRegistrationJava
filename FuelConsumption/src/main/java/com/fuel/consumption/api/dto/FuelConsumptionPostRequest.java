package com.fuel.consumption.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.util.FuelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@ApiModel(description="All details about the fuel consumption. ")
@Data
@NoArgsConstructor
public class FuelConsumptionPostRequest {

    @ApiModelProperty(notes="Fuel Type is mandatory attribute, have to be specified type, for take specified types use 'fuelTypes endpoint'")
    private String fuelType;

    @ApiModelProperty(notes="Fuel Price is mandatory attribute, have to be positive number")
    private BigDecimal fuelPrice;

    @ApiModelProperty(notes="Fuel Volume is mandatory attribute, have to be positive number")
    private BigDecimal fuelVolume;

    @ApiModelProperty(notes="Fuel Consumption Date is mandatory attribute, have to be  yyyy-MM-dd date format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate consumptionDate;

    @ApiModelProperty(notes="Driver Id is mandatory attribute, have to be positive number")
    private Long driverId;

    public static FuelConsumptionPostRequest toDto(FuelConsumption entity){
        FuelConsumptionPostRequest dto = new FuelConsumptionPostRequest();
        dto.setFuelType(entity.getFuelType().name());
        dto.setFuelPrice(entity.getFuelPrice());
        dto.setFuelVolume(entity.getFuelVolume());
        dto.setConsumptionDate(entity.getConsumptionDate());
        dto.setDriverId(entity.getDriverId());
        return dto;
    }

    public static FuelConsumption toEntity(FuelConsumptionPostRequest dto){
        FuelConsumption entity = new FuelConsumption();
        entity.setFuelType(FuelType.valueOf(dto.getFuelType()));
        entity.setFuelPrice(dto.getFuelPrice());
        entity.setFuelVolume(dto.getFuelVolume());
        entity.setConsumptionDate(dto.getConsumptionDate());
        entity.setDriverId(dto.getDriverId());
        return entity;
    }

    public FuelConsumptionPostRequest(String fuelType, BigDecimal fuelPrice, BigDecimal fuelVolume, LocalDate consumptionDate, Long driverId) {
        this.fuelType = fuelType;
        this.fuelPrice = fuelPrice;
        this.fuelVolume = fuelVolume;
        this.consumptionDate = consumptionDate;
        this.driverId = driverId;
    }
}
