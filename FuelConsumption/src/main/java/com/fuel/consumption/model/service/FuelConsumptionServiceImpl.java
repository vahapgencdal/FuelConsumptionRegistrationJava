package com.fuel.consumption.model.service;

import com.fuel.consumption.api.dto.FuelConsumptionDto;
import com.fuel.consumption.api.dto.FuelConsumptionMonthlyStatisticDto;
import com.fuel.consumption.api.dto.FuelConsumptionRecordSpecifiedByMonthDto;
import com.fuel.consumption.api.dto.TotalSpentAmountOfMoneyDto;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FuelConsumptionServiceImpl implements FuelConsumptionService {

    @Autowired
    private FuelConsumptionRepository fuelConsumptionRepository;


    @Override
    public void insert(FuelConsumptionDto fuelConsumptionDto) {
        fuelConsumptionRepository.save(FuelConsumptionDto.toEntity(fuelConsumptionDto));
    }

    @Override
    public void bulkInsert(List<FuelConsumptionDto> fuelConsumptionDtoList) {
        List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList
                .stream()
                .map(FuelConsumptionDto::toEntity)
                .collect(Collectors.toList());
            fuelConsumptionRepository.saveAll(fuelConsumptionList);
    }

    @Override
    public Map<Integer, BigDecimal> findTotalSpentAmountOfMoneyGroupedByMonth() {
        List<TotalSpentAmountOfMoneyDto> items = fuelConsumptionRepository.findAllTotalSpentAmountOfMoney();

        return items
                .stream()
                .collect(Collectors.groupingBy(TotalSpentAmountOfMoneyDto::getMonth))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue()
                                .stream()
                                .map(x -> x.getFuelPrice().multiply(x.getFuelVolume()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .setScale(BigDecimalUtil.SCALE, BigDecimalUtil.ROUNDING_MODE)
                        )
                );
    }

    @Override
    public Map<Integer, BigDecimal> findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(long driverId) {
        List<TotalSpentAmountOfMoneyDto> items = fuelConsumptionRepository.findAllTotalSpentAmountOfMoneyByDriverId(driverId);

        return items
                .stream()
                .collect(Collectors.groupingBy(TotalSpentAmountOfMoneyDto::getMonth))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue()
                                .stream()
                                .map(x -> x.getFuelPrice().multiply(x.getFuelVolume()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .setScale(BigDecimalUtil.SCALE, BigDecimalUtil.ROUNDING_MODE)
                        )
                );
    }

    @Override
    public List<FuelConsumptionRecordSpecifiedByMonthDto> findFuelConsumptionRecordsByMonth(int month) {
        return null;
    }

    @Override
    public List<FuelConsumptionRecordSpecifiedByMonthDto> findFuelConsumptionRecordsByMonthAndDriverId(int month, int driverId) {
        return null;
    }

    @Override
    public List<FuelConsumptionMonthlyStatisticDto> findEachMonthFuelConsumptionStatisticsGroupedByFuelType() {
        return null;
    }

    @Override
    public List<FuelConsumptionMonthlyStatisticDto> findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(int driverId) {
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
