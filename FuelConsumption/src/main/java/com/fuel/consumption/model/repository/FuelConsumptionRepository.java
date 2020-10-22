package com.fuel.consumption.model.repository;

import com.fuel.consumption.api.dto.ExpenseReportDto;
import com.fuel.consumption.model.entity.FuelConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

public interface FuelConsumptionRepository extends JpaRepository<FuelConsumption, Long>,
        JpaSpecificationExecutor<FuelConsumption> {


    // @formatter:off
    @Query(value = "select new com.fuel.consumption.api.dto.ExpenseReportDto(EXTRACT(MONTH FROM fuelConsumption.consumptionDate)" +
            ",fuelConsumption.fuelPrice , fuelConsumption.fuelVolume)" +
            "from FuelConsumption fuelConsumption")
    // @formatter:on
    List<ExpenseReportDto> findExpenseReportByPeriod();

    // @formatter:off
    @Query(value = "select new com.fuel.consumption.api.dto.ExpenseReportDto(EXTRACT(MONTH FROM fuelConsumption.consumptionDate)" +
            ",fuelConsumption.fuelPrice , fuelConsumption.fuelVolume, fuelConsumption.driverId)" +
            "from FuelConsumption fuelConsumption where fuelConsumption.driverId = :theDriverId")
    // @formatter:on
    List<ExpenseReportDto> findExpenseReportByPeriodByDriverId(@Param("theDriverId") long driverId);

    List<FuelConsumption> findByDriverId(long driverId);

}