package com.fuel.consumption.model.service;

import com.fuel.consumption.api.dto.FuelConsumptionDto;
import com.fuel.consumption.api.dto.FuelConsumptionMonthlyStatisticDto;
import com.fuel.consumption.api.dto.FuelConsumptionRecordSpecifiedByMonthDto;
import com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FuelConsumptionService {

    void insert(FuelConsumptionDto fuelConsumptionDto);

    void bulkInsert(List<FuelConsumptionDto> fuelConsumptionDtoList);

    Map<Integer, BigDecimal> findTotalSpentAmountOfMoneyGroupedByMonth();

    Map<Integer, BigDecimal> findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(long driverId);

    List<FuelConsumptionRecordSpecifiedByMonthDto> findFuelConsumptionRecordsByMonth(int month);

    List<FuelConsumptionRecordSpecifiedByMonthDto> findFuelConsumptionRecordsByMonthAndDriverId(int month, int driverId);

    List<FuelConsumptionMonthlyStatisticDto> findEachMonthFuelConsumptionStatisticsGroupedByFuelType();

    List<FuelConsumptionMonthlyStatisticDto> findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(int driverId);

    List<FuelConsumptionDto> findAll();

    List<FuelConsumptionDto> findAllByDriverId(long driverId);
}
