package com.fuel.consumption.model.service;

import com.fuel.consumption.api.dto.FuelConsumptionDto;
import com.fuel.consumption.api.dto.FuelConsumptionMonthlyStatisticDto;
import com.fuel.consumption.model.entity.FuelConsumption;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FuelConsumptionService {

    /*
Data validation should be performed before persisting and corresponding error message should be returned in case of invalid input.
Fuel consumption data retrieval should provide (as a separate endpoint):
· total spent amount of money grouped by month
· list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)
· statistics for each month, list fuel consumption records grouped by fuel type (each row should contain: fuel type, volume, average price, total price)
Every request can be made either for all drivers or for the specific driver (identified by id).
    * */

    void insert(FuelConsumptionDto fuelConsumptionDto);

    void bulkInsert(List<FuelConsumptionDto> fuelConsumptionDtoList);


    BigDecimal totalSpentAmountOfMoneyGroupedByMonth(int monthNumber);

    List<FuelConsumptionMonthlyStatisticDto> statisticsForEachMonth(int year);


    List<FuelConsumptionDto> findAll();

    List<FuelConsumptionDto> findAllByDriverId(long driverId);
}
