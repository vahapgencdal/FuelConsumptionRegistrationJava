package com.fuel.consumption.api.dto;

import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.util.BigDecimalUtil;
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
public class FuelConsumptionRecordSpecifiedByMonthDto {

      private String fuelType;
      private BigDecimal fuelVolume;
      private BigDecimal fuelPrice;
      private BigDecimal totalPrice;
      private LocalDate consumptionDate;
      private long driverId;

      public static FuelConsumptionRecordSpecifiedByMonthDto toDto(FuelConsumption entity){
            FuelConsumptionRecordSpecifiedByMonthDto dto =  new FuelConsumptionRecordSpecifiedByMonthDto();
            dto.setFuelType(entity.getFuelType().name());
            dto.setFuelVolume(entity.getFuelVolume());
            dto.setFuelPrice(entity.getFuelPrice());
            dto.setTotalPrice(entity.getFuelPrice().multiply(entity.getFuelVolume())
                    .setScale(BigDecimalUtil.SCALE,BigDecimalUtil.ROUNDING_MODE));
            dto.setConsumptionDate(entity.getConsumptionDate());
            dto.setDriverId(entity.getDriverId());
            return dto;
      }

}
