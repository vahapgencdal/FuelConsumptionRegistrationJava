package com.fuel.consumption.model.entity;

import com.fuel.consumption.util.FuelType;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

}