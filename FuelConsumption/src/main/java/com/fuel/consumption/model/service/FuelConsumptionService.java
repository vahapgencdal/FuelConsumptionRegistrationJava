package com.fuel.consumption.model.service;

import com.fuel.consumption.api.dto.*;
import com.fuel.consumption.model.entity.FuelConsumption;

import java.util.List;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

public interface FuelConsumptionService {

    FuelConsumption insert(FuelConsumptionPostRequest fuelConsumptionDto);

    List<FuelConsumption> insertAll(List<FuelConsumptionPostRequest> fuelConsumptionDtoList);

    List<TotalSpentAmountOfMoneyResponse> findTotalSpentAmountOfMoneyGroupedByMonth();

    List<TotalSpentAmountOfMoneyResponse> findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(long driverId);

    List<FuelConsumptionRecordSpecifiedByMonthDto> findFuelConsumptionRecordsByMonth(int month);

    List<FuelConsumptionRecordSpecifiedByMonthDto> findFuelConsumptionRecordsByMonthAndDriverId(int month, long driverId);

    List<FuelConsumptionStatisticResponse> findEachMonthFuelConsumptionStatisticsGroupedByFuelType();

    List<FuelConsumptionStatisticResponse> findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(long driverId);

    List<FuelConsumptionPostRequest> findAll();

    List<FuelConsumptionPostRequest> findAllByDriverId(long driverId);
}
