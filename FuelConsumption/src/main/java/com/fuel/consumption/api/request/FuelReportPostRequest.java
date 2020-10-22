package com.fuel.consumption.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuel.consumption.api.validator.ValueOfEnum;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.util.FuelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@ApiModel(description = "All details about the fuel consumption. ")
@Data
@NoArgsConstructor
public class FuelReportPostRequest {

    @ApiModelProperty(notes = "Fuel Type is mandatory attribute, have to be specified type. Fuel Types: DIESEL,P95,P98 and LPG")
    @NotNull
    @ValueOfEnum(enumClass = FuelType.class, message = "Must be specified types. DIESEL,P95,P98 and LPG")
    private String fuelType;

    @ApiModelProperty(notes = "Fuel Price is mandatory attribute, have to be positive number")
    @NotNull
    @Positive(message = "FuelPrice have to be positive number")
    private BigDecimal fuelPrice;

    @ApiModelProperty(notes = "Fuel Volume is mandatory attribute, have to be positive number")
    @NotNull
    @Positive(message = "FuelVolume have to be positive number")
    private BigDecimal fuelVolume;

    @ApiModelProperty(notes = "Consumption Date is mandatory attribute, have to be  yyyy-MM-dd date format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate consumptionDate;

    @ApiModelProperty(notes = "Driver ID is mandatory attribute, have to be positive number")
    @NotNull
    @Positive(message = "Driver ID have to be positive number")
    private Long driverId;

    public static FuelReportPostRequest toDto(FuelConsumption entity) {
        FuelReportPostRequest dto = new FuelReportPostRequest();
        dto.setFuelType(entity.getFuelType().name());
        dto.setFuelPrice(entity.getFuelPrice());
        dto.setFuelVolume(entity.getFuelVolume());
        dto.setConsumptionDate(entity.getConsumptionDate());
        dto.setDriverId(entity.getDriverId());
        return dto;
    }

    public static FuelConsumption toEntity(FuelReportPostRequest dto) {
        FuelConsumption entity = new FuelConsumption();
        entity.setFuelType(FuelType.valueOf(dto.getFuelType()));
        entity.setFuelPrice(dto.getFuelPrice());
        entity.setFuelVolume(dto.getFuelVolume());
        entity.setConsumptionDate(dto.getConsumptionDate());
        entity.setDriverId(dto.getDriverId());
        return entity;
    }

    public FuelReportPostRequest(String fuelType, BigDecimal fuelPrice, BigDecimal fuelVolume, LocalDate consumptionDate, Long driverId) {
        this.fuelType = fuelType;
        this.fuelPrice = fuelPrice;
        this.fuelVolume = fuelVolume;
        this.consumptionDate = consumptionDate;
        this.driverId = driverId;
    }
}
