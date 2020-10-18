package com.fuel.consumption.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FuelConsumptionRecordSpecifiedByMonthDto {

      private String fuelType;
      private BigDecimal volume;
      private LocalDate volumeDate;
      private BigDecimal volumePrice;
      private BigDecimal totalPrice;
      private long driverId;

}
