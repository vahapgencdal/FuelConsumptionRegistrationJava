package com.fuel.consumption.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.util.FuelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel(description="All details about the fuel consumption. ")
@Data
public class FuelConsumptionDto {

    private Long id;

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

    public static FuelConsumptionDto toDto(FuelConsumption entity){
        FuelConsumptionDto dto = new FuelConsumptionDto();
        dto.setId(entity.getId());
        dto.setFuelType(entity.getFuelType().name());
        dto.setFuelPrice(entity.getFuelPrice());
        dto.setFuelVolume(entity.getFuelVolume());
        dto.setConsumptionDate(entity.getConsumptionDate());
        dto.setDriverId(entity.getDriverId());
        return dto;
    }

    public static FuelConsumption toEntity(FuelConsumptionDto dto){
        FuelConsumption entity = new FuelConsumption();
        entity.setFuelType(FuelType.valueOf(dto.getFuelType()));
        entity.setFuelPrice(dto.getFuelPrice().setScale(4,BigDecimal.ROUND_CEILING));
        entity.setFuelVolume(dto.getFuelVolume().setScale(4,BigDecimal.ROUND_CEILING));
        entity.setConsumptionDate(dto.getConsumptionDate());
        entity.setDriverId(dto.getDriverId());
        return entity;
    }
}
