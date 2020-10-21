package com.fuel.consumption.model.entity;

import com.fuel.consumption.util.FuelType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "FUEL_CONSUMPTION")
public class FuelConsumption extends BaseEntity {

    @Column(name = "FUEL_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(name = "FUEL_PRICE", nullable = false)
    private BigDecimal fuelPrice;

    @Column(name = "FUEL_VOLUME", nullable = false)
    private BigDecimal fuelVolume;

    @Column(name = "CONSUMPTION_DATE", nullable = false)
    private LocalDate consumptionDate;

    @Column(name = "DRIVER_ID", nullable = false)
    private Long driverId;

    public FuelConsumption(FuelType fuelType, BigDecimal fuelPrice, BigDecimal fuelVolume, LocalDate consumptionDate, Long driverId) {
        this.fuelType = fuelType;
        this.fuelPrice = fuelPrice;
        this.fuelVolume = fuelVolume;
        this.consumptionDate = consumptionDate;
        this.driverId = driverId;
    }
}