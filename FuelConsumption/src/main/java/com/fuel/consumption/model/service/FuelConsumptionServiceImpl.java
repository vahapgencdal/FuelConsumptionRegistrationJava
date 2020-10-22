package com.fuel.consumption.model.service;

import com.fuel.consumption.api.request.FuelReportPostRequest;
import com.fuel.consumption.api.response.ExpenseReportResponse;
import com.fuel.consumption.api.response.FuelReportFuelTypeResponse;
import com.fuel.consumption.api.response.FuelReportMonthlyResponse;
import com.fuel.consumption.api.response.FuelReportResponse;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.util.BigDecimalUtil;
import com.fuel.consumption.util.GroupByType;
import com.fuel.consumption.util.ReportPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public FuelConsumption insert(FuelReportPostRequest fuelConsumptionDto) {
        return fuelConsumptionRepository.save(FuelReportPostRequest.toEntity(fuelConsumptionDto));
    }

    @Override
    public List<FuelConsumption> insertAll(List<FuelReportPostRequest> fuelConsumptionDtoList) {
        List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList
                .stream()
                .map(FuelReportPostRequest::toEntity)
                .collect(Collectors.toList());
        return fuelConsumptionRepository.saveAll(fuelConsumptionList);
    }

    @Override
    public List<ExpenseReportResponse> findExpenseReportByPeriod(String period) {

        List<FuelConsumption> items = fuelConsumptionRepository.findAll();

        return getExpenseReportByPeriod(items, period);
    }

    @Override
    public List<ExpenseReportResponse> findExpenseReportByPeriodAndDriverId(long driverId, String period) {
        List<FuelConsumption> items = fuelConsumptionRepository.findByDriverId(driverId);

        return getExpenseReportByPeriod(items, period);
    }

    private List<ExpenseReportResponse> getExpenseReportByPeriod(List<FuelConsumption> items, final String period) {
        return items
                .stream()
                .collect(Collectors.groupingBy(fc -> {
                    return ReportPeriod.MONTH.name().equals(period) ?
                            fc.getConsumptionDate().getMonth().getValue() :
                            fc.getConsumptionDate().getYear();
                }))
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
                .map(entry -> ExpenseReportResponse.toResponse(entry, period))
                .collect(Collectors.toList());
    }


    @Override
    public List<FuelReportResponse> fuelReports(int month) {
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
    public List<FuelReportResponse> fuelReports(int month, long driverId) {
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

    private List<FuelReportResponse> getGroupOfFullConsumptionList(List<FuelConsumption> fuelConsumptionList) {
        return fuelConsumptionList.stream()
                .map(FuelReportResponse::toDto)
                .sorted(Comparator.comparing(FuelReportResponse::getConsumptionDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<FuelReportMonthlyResponse> monthlyFuelReports(String groupBy) {

        List<FuelConsumption> fuelConsumptionList = fuelConsumptionRepository.findAll();

        return getStatisticOfMonthlyFuelReports(fuelConsumptionList, groupBy);
    }

    @Override
    public List<FuelReportMonthlyResponse> monthlyFuelReports(long driverId, String groupBy) {

        List<FuelConsumption> fuelConsumptionList = fuelConsumptionRepository.findByDriverId(driverId);

        return getStatisticOfMonthlyFuelReports(fuelConsumptionList, groupBy);
    }

    private List<FuelReportMonthlyResponse> getStatisticOfMonthlyFuelReports(List<FuelConsumption> fuelConsumptions, final String groupBy) {
        return fuelConsumptions.stream()
                .collect(Collectors.groupingBy(fuelConsumption -> fuelConsumption.getConsumptionDate().getMonth().getValue()))
                .entrySet().stream()
                .map(integerListEntry -> {
                    List<FuelReportFuelTypeResponse> fuelTypeConsumptions = integerListEntry.getValue()
                            .stream()
                            .collect(Collectors.groupingBy(fuelConsumption -> getValueOfGroupBy(groupBy, fuelConsumption)))
                            .entrySet().stream()
                            .map(FuelReportFuelTypeResponse::toResponse)
                            .collect(Collectors.toList());

                    return FuelReportMonthlyResponse.toResponse(integerListEntry.getKey(), fuelTypeConsumptions);
                }).collect(Collectors.toList());
    }

    public static final String getValueOfGroupBy(String groupBy, FuelConsumption fuelConsumption) {
        if (GroupByType.FUEL_TYPE.name().equals(groupBy)) {
            return fuelConsumption.getFuelType().name();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<FuelReportPostRequest> findAll() {
        return fuelConsumptionRepository.findAll()
                .stream()
                .map(FuelReportPostRequest::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuelReportPostRequest> findAllByDriverId(long driverId) {
        return fuelConsumptionRepository.findByDriverId(driverId)
                .stream()
                .map(FuelReportPostRequest::toDto)
                .collect(Collectors.toList());

    }
}
