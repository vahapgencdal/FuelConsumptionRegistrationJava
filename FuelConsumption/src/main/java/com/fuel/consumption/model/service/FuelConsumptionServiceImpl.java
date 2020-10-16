package com.fuel.consumption.model.service;

import com.fuel.consumption.api.dto.FuelConsumptionDto;
import com.fuel.consumption.api.dto.FuelConsumptionMonthlyStatisticDto;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FuelConsumptionServiceImpl implements FuelConsumptionService {

    @Autowired
    private FuelConsumptionRepository fuelConsumptionRepository;


    @Override
    public void insert(FuelConsumptionDto fuelConsumptionDto) {

    }

    @Override
    public void bulkInsert(List<FuelConsumptionDto> fuelConsumptionDtoList) {

    }

    @Override
    public BigDecimal totalSpentAmountOfMoneyGroupedByMonth(int monthNumber) {
        return null;
    }

    @Override
    public List<FuelConsumptionMonthlyStatisticDto> statisticsForEachMonth(int year) {
        return null;
    }

    @Override
    public List<FuelConsumptionDto> findAll() {
        return null;
    }

    @Override
    public List<FuelConsumptionDto> findAllByDriverId(long driverId) {
        return null;
    }
}
