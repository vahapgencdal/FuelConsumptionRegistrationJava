package com.fuel.consumption.model.service;

import com.fuel.consumption.api.request.FuelReportPostRequest;
import com.fuel.consumption.api.response.ExpenseReportResponse;
import com.fuel.consumption.api.response.FuelReportMonthlyResponse;
import com.fuel.consumption.api.response.FuelReportResponse;
import com.fuel.consumption.model.entity.FuelConsumption;

import java.util.List;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

public interface FuelConsumptionService {

    FuelConsumption insert(FuelReportPostRequest fuelConsumptionDto);

    List<FuelConsumption> insertAll(List<FuelReportPostRequest> fuelConsumptionDtoList);

    List<ExpenseReportResponse> findExpenseReportByPeriod(String period);

    List<ExpenseReportResponse> findExpenseReportByPeriodAndDriverId(long driverId, String period);

    List<FuelReportResponse> fuelReports(int month);

    List<FuelReportResponse> fuelReports(int month, long driverId);

    List<FuelReportMonthlyResponse> monthlyFuelReports(String groupBy);

    List<FuelReportMonthlyResponse> monthlyFuelReports(long driverId, String groupBy);

    List<FuelReportPostRequest> findAll();

    List<FuelReportPostRequest> findAllByDriverId(long driverId);
}
