package com.fuel.consumption.model.repository;

import com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyDto;
import com.fuel.consumption.model.entity.FuelConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

import java.util.List;

public interface FuelConsumptionRepository extends JpaRepository<FuelConsumption, Long>,
        JpaSpecificationExecutor<FuelConsumption> {


    // @formatter:off
    @Query(value = "select new com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyDto(EXTRACT(MONTH FROM fuelConsumption.consumptionDate)" +
            ",fuelConsumption.fuelPrice , fuelConsumption.fuelVolume)" +
            "from FuelConsumption fuelConsumption")
    // @formatter:on
    List<TotalSpentAmountOfMoneyDto> findAllTotalSpentAmountOfMoney();

    // @formatter:off
    @Query(value = "select new com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyDto(EXTRACT(MONTH FROM fuelConsumption.consumptionDate)" +
            ",fuelConsumption.fuelPrice , fuelConsumption.fuelVolume, fuelConsumption.driverId)" +
            "from FuelConsumption fuelConsumption where fuelConsumption.driverId = :theDriverId")
    // @formatter:on
    List<TotalSpentAmountOfMoneyDto> findAllTotalSpentAmountOfMoneyByDriverId(@Param("theDriverId") long driverId);

    List<FuelConsumption> findByDriverId(long driverId);

}