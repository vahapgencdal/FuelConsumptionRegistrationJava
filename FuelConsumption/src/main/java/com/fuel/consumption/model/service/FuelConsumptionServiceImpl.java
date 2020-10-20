package com.fuel.consumption.model.service;

import com.fuel.consumption.api.dto.*;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@Service
public class FuelConsumptionServiceImpl implements FuelConsumptionService {

    @Autowired
    private FuelConsumptionRepository fuelConsumptionRepository;


    @Override
    public FuelConsumption insert(FuelConsumptionPostRequest fuelConsumptionDto) {
        return fuelConsumptionRepository.save(FuelConsumptionPostRequest.toEntity(fuelConsumptionDto));
    }

    @Override
    public List<FuelConsumption> insertAll(List<FuelConsumptionPostRequest> fuelConsumptionDtoList) {
        List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList
                .stream()
                .map(FuelConsumptionPostRequest::toEntity)
                .collect(Collectors.toList());
            return fuelConsumptionRepository.saveAll(fuelConsumptionList);
    }

    @Override
    public List<TotalSpentAmountOfMoneyResponse> findTotalSpentAmountOfMoneyGroupedByMonth() {
        List<TotalSpentAmountOfMoneyDto> items = fuelConsumptionRepository.findAllTotalSpentAmountOfMoney();

        return groupTotalSpentAmountOfMoney(items);
    }

    @Override
    public List<TotalSpentAmountOfMoneyResponse> findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(long driverId) {
        List<TotalSpentAmountOfMoneyDto> items = fuelConsumptionRepository.findAllTotalSpentAmountOfMoneyByDriverId(driverId);

        return groupTotalSpentAmountOfMoney(items);
    }

    private List<TotalSpentAmountOfMoneyResponse> groupTotalSpentAmountOfMoney(List<TotalSpentAmountOfMoneyDto> items) {
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
                )
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(TotalSpentAmountOfMoneyResponse::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FuelConsumptionRecordSpecifiedByMonthDto> findFuelConsumptionRecordsByMonth(int month)  {
        List<FuelConsumption> fuelConsumptionList =
                fuelConsumptionRepository.findAll(
                        (Specification<FuelConsumption>) (root, query, cb) -> {

                            List<Predicate> predicates = new ArrayList<>();

                            predicates.add(cb.equal(cb.function("MONTH", Integer.class, root.get("consumptionDate")), month));

                            return cb.and(predicates.toArray(new Predicate[0]));
                });

        return getGroupOfFullConsumptionList(fuelConsumptionList);
    }

    @Override
    public List<FuelConsumptionRecordSpecifiedByMonthDto> findFuelConsumptionRecordsByMonthAndDriverId(int month, long driverId) {
        List<FuelConsumption> fuelConsumptionList =
                fuelConsumptionRepository.findAll(
                        (Specification<FuelConsumption>) (root, query, cb) -> {

                            List<Predicate> predicates = new ArrayList<>();

                            predicates.add(cb.equal(cb.function("MONTH", Integer.class, root.get("consumptionDate")), month));

                            predicates.add(cb.equal(root.get("driverId"), driverId));

                            return cb.and(predicates.toArray(new Predicate[0]));
                        });

        return getGroupOfFullConsumptionList(fuelConsumptionList);
    }

    private List<FuelConsumptionRecordSpecifiedByMonthDto> getGroupOfFullConsumptionList(List<FuelConsumption> fuelConsumptionList){
        return fuelConsumptionList.stream()
                .map(FuelConsumptionRecordSpecifiedByMonthDto::toDto)
                .sorted(Comparator.comparing(FuelConsumptionRecordSpecifiedByMonthDto::getConsumptionDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<FuelConsumptionStatisticResponse> findEachMonthFuelConsumptionStatisticsGroupedByFuelType() {

        List<FuelConsumption> fuelConsumptionList = fuelConsumptionRepository.findAll();

        return getStatisticOfFuelConsumptionListByFuelTypeAndMonth(fuelConsumptionList);
    }

    @Override
    public List<FuelConsumptionStatisticResponse> findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(long driverId) {

        List<FuelConsumption> fuelConsumptionList  = fuelConsumptionRepository.findByDriverId(driverId);

        return getStatisticOfFuelConsumptionListByFuelTypeAndMonth(fuelConsumptionList);
    }

    private List<FuelConsumptionStatisticResponse> getStatisticOfFuelConsumptionListByFuelTypeAndMonth(List<FuelConsumption> fuelConsumptions){
        return fuelConsumptions.stream()
                .collect(Collectors.groupingBy(fuelConsumption -> fuelConsumption.getConsumptionDate().getMonth().getValue()))
                .entrySet().stream()
                .map(integerListEntry -> {
                    List<FuelConsumptionStatisticFuelTypeResponse> fuelTypeConsumptions = integerListEntry.getValue()
                            .stream()
                            .collect(Collectors.groupingBy(FuelConsumption::getFuelType))
                            .entrySet().stream()
                            .map(FuelConsumptionStatisticFuelTypeResponse::toResponse)
                            .collect(Collectors.toList());

                    return FuelConsumptionStatisticResponse.toResponse(integerListEntry.getKey(),fuelTypeConsumptions);
                }).collect(Collectors.toList());
    }

    @Override
    public List<FuelConsumptionPostRequest> findAll() {
        return fuelConsumptionRepository.findAll()
                .stream()
                .map(FuelConsumptionPostRequest::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuelConsumptionPostRequest> findAllByDriverId(long driverId) {
        return fuelConsumptionRepository.findByDriverId(driverId)
                .stream()
                .map(FuelConsumptionPostRequest::toDto)
                .collect(Collectors.toList());

    }
}
