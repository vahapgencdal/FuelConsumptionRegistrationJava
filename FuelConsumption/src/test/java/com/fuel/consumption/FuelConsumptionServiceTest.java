package com.fuel.consumption;

import com.fuel.consumption.api.dto.*;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.repository.FuelConsumptionRepository;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.BigDecimalUtil;
import com.fuel.consumption.util.FuelType;
import com.fuel.consumption.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@ActiveProfiles("logback-test")
@Slf4j
class FuelConsumptionServiceTest {

	@MockBean
	private FuelConsumptionRepository fuelConsumptionRepository;

	@Autowired
	private FuelConsumptionService fuelConsumptionService;

	@Test
	void insert_success_case() {
			FuelConsumptionPostRequest fc = new FuelConsumptionPostRequest();
			fc.setDriverId(123L);
			fc.setFuelType(FuelType.DIESEL.name());
			when(fuelConsumptionRepository.save(any())).thenReturn(FuelConsumptionPostRequest.toEntity(fc));
			FuelConsumption consumption = fuelConsumptionService.insert(fc);
			Assertions.assertEquals(fc.getFuelType(),consumption.getFuelType().name());
	}

	@Test
	void bulk_insert_success_case() {
		FuelConsumptionPostRequest fcd = new FuelConsumptionPostRequest();
		fcd.setDriverId(123L);
		fcd.setFuelType(FuelType.DIESEL.name());
		FuelConsumptionPostRequest fcd2 = new FuelConsumptionPostRequest();
		fcd2.setDriverId(1234L);
		fcd2.setFuelType(FuelType.LPG.name());
		List<FuelConsumptionPostRequest> consumptionDtoList = new ArrayList<>();
		consumptionDtoList.add(fcd2);
		consumptionDtoList.add(fcd);
		when(fuelConsumptionRepository.saveAll(any())).
				thenReturn(consumptionDtoList.stream().map(FuelConsumptionPostRequest::toEntity)
						.collect(Collectors.toList()));
		List<FuelConsumption> insertedConsumptionList = fuelConsumptionService.insertAll(consumptionDtoList);
		Assertions.assertEquals(consumptionDtoList.size(), insertedConsumptionList.size());
	}

	@Test
	void findTotalSpentAmountOfMoneyGroupedByMonth_success_case() throws IOException {
		List<FuelConsumptionPostRequest> fuelConsumptionDtoList= JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/findAllTotalSpentAmountOfMoneyServiceTest.json");
		List<TotalSpentAmountOfMoneyDto> totalSpentAmountOfMoneyDtoList = fuelConsumptionDtoList.stream().map(consumption -> {
			return new TotalSpentAmountOfMoneyDto(consumption.getConsumptionDate().getMonth().getValue(),
					consumption.getFuelPrice(),
					consumption.getFuelVolume());
		}).collect(Collectors.toList());


		List<TotalSpentAmountOfMoneyResponse> expectedResult = getTotalSpentAmountOfMoneyResponseList(totalSpentAmountOfMoneyDtoList);

		when(fuelConsumptionRepository.findAllTotalSpentAmountOfMoney()).thenReturn(totalSpentAmountOfMoneyDtoList);

		List<TotalSpentAmountOfMoneyResponse> actualResult = fuelConsumptionService.findTotalSpentAmountOfMoneyGroupedByMonth();
		Assertions.assertEquals(expectedResult.size(),actualResult.size());
		Assertions.assertEquals(expectedResult.get(0).getMonth(),actualResult.get(0).getMonth());
		Assertions.assertEquals(expectedResult.get(0).getTotalAmount(),actualResult.get(0).getTotalAmount());

		Assertions.assertEquals(expectedResult.get(1).getMonth(),actualResult.get(1).getMonth());
		Assertions.assertEquals(expectedResult.get(1).getTotalAmount(),actualResult.get(1).getTotalAmount());

		Assertions.assertEquals(expectedResult.get(2).getMonth(),actualResult.get(2).getMonth());
		Assertions.assertEquals(expectedResult.get(2).getTotalAmount(),actualResult.get(2).getTotalAmount());
	}

	@Test
	void findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth_success_case() throws IOException {
		long driverId = 123L;
		List<FuelConsumptionPostRequest> fuelConsumptionDtoList= JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/findAllTotalSpentAmountOfMoneyByDriverIdServiceTest.json");
		List<TotalSpentAmountOfMoneyDto> totalSpentAmountOfMoneyDtoList = fuelConsumptionDtoList.stream()
				.filter(fuelConsumptionDto -> driverId==fuelConsumptionDto.getDriverId())
				.map(consumption -> {
			return new TotalSpentAmountOfMoneyDto(consumption.getConsumptionDate().getMonth().getValue(),
					consumption.getFuelPrice(),
					consumption.getFuelVolume());
		}).collect(Collectors.toList());


		List<TotalSpentAmountOfMoneyResponse> expectedResult = getTotalSpentAmountOfMoneyResponseList(totalSpentAmountOfMoneyDtoList);

		when(fuelConsumptionRepository.findAllTotalSpentAmountOfMoneyByDriverId(driverId)).thenReturn(totalSpentAmountOfMoneyDtoList);

		List<TotalSpentAmountOfMoneyResponse> actualResult = fuelConsumptionService.findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(driverId);
		Assertions.assertEquals(expectedResult.size(),actualResult.size());
		Assertions.assertEquals(expectedResult.get(0).getMonth(),actualResult.get(0).getMonth());
		Assertions.assertEquals(expectedResult.get(0).getTotalAmount(),actualResult.get(0).getTotalAmount());

		Assertions.assertEquals(expectedResult.get(1).getMonth(),actualResult.get(1).getMonth());
		Assertions.assertEquals(expectedResult.get(1).getTotalAmount(),actualResult.get(1).getTotalAmount());

		Assertions.assertEquals(expectedResult.get(2).getMonth(),actualResult.get(2).getMonth());
		Assertions.assertEquals(expectedResult.get(2).getTotalAmount(),actualResult.get(2).getTotalAmount());
	}

	private List<TotalSpentAmountOfMoneyResponse> getTotalSpentAmountOfMoneyResponseList(List<TotalSpentAmountOfMoneyDto> totalSpentAmountOfMoneyDtoList){
		return totalSpentAmountOfMoneyDtoList
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


	@Test
	void findFuelConsumptionRecordsByMonth_success_case() throws IOException {
		int monthId=3;
		List<FuelConsumptionPostRequest> fuelConsumptionDtoList= JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/general_test_data.json");
		List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList
				.stream()
				.filter(fuelConsumption -> fuelConsumption.getConsumptionDate().getMonth().getValue() == monthId)
				.map(FuelConsumptionPostRequest::toEntity)
				.collect(Collectors.toList());

		when(fuelConsumptionRepository.findAll((Specification<FuelConsumption>) any())).thenReturn(fuelConsumptionList);

		List<FuelConsumptionRecordSpecifiedByMonthDto> expectedResult = fuelConsumptionService.findFuelConsumptionRecordsByMonth(monthId);

		List<FuelConsumptionRecordSpecifiedByMonthDto> actualResult = getGroupOfFullConsumptionList(fuelConsumptionList);

		Assertions.assertEquals(expectedResult.size(),actualResult.size());
		Assertions.assertEquals(expectedResult.get(0).getTotalPrice(),actualResult.get(0).getTotalPrice());
		Assertions.assertEquals(expectedResult.get(1).getTotalPrice(),actualResult.get(1).getTotalPrice());
	}


	private List<FuelConsumptionRecordSpecifiedByMonthDto> getGroupOfFullConsumptionList(List<FuelConsumption> fuelConsumptionList){
		return fuelConsumptionList.stream()
				.map(FuelConsumptionRecordSpecifiedByMonthDto::toDto)
				.sorted(Comparator.comparing(FuelConsumptionRecordSpecifiedByMonthDto::getConsumptionDate))
				.collect(Collectors.toList());
	}

	@Test
	void findFuelConsumptionRecordsByMonthAndDriverId_success_case() throws IOException {
		int monthId=2;
		long driverId=3;
		List<FuelConsumptionPostRequest> fuelConsumptionDtoList= JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/general_test_data.json");
		List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList
				.stream()
				.filter(fuelConsumption -> fuelConsumption.getConsumptionDate().getMonth().getValue() == monthId)
				.filter(fuelConsumption -> fuelConsumption.getDriverId() == driverId)
				.map(FuelConsumptionPostRequest::toEntity)
				.collect(Collectors.toList());

		when(fuelConsumptionRepository.findAll((Specification<FuelConsumption>) any())).thenReturn(fuelConsumptionList);

		List<FuelConsumptionRecordSpecifiedByMonthDto> expectedResult = fuelConsumptionService.findFuelConsumptionRecordsByMonthAndDriverId(monthId,driverId);

		List<FuelConsumptionRecordSpecifiedByMonthDto> actualResult = getGroupOfFullConsumptionList(fuelConsumptionList);

		Assertions.assertEquals(expectedResult.size(),actualResult.size());
		Assertions.assertEquals(expectedResult.get(0).getTotalPrice(),actualResult.get(0).getTotalPrice());
		Assertions.assertEquals(expectedResult.get(1).getTotalPrice(),actualResult.get(1).getTotalPrice());

	}
	@Test
	void findEachMonthFuelConsumptionStatisticsGroupedByFuelType_success_case() throws IOException {

		List<FuelConsumptionPostRequest> fuelConsumptionDtoList= JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/general_test_data.json");
		List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList
				.stream()
				.map(FuelConsumptionPostRequest::toEntity)
				.collect(Collectors.toList());

		when(fuelConsumptionRepository.findAll()).thenReturn(fuelConsumptionList);

		List<FuelConsumptionStatisticResponse> er = fuelConsumptionService.findEachMonthFuelConsumptionStatisticsGroupedByFuelType();

		List<FuelConsumptionStatisticResponse> ar = getStatisticOfFuelConsumptionListByFuelTypeAndMonth(fuelConsumptionList);

		Assertions.assertEquals(er.size(),ar.size());
		Assertions.assertEquals(er.get(0).getMonth(),ar.get(0).getMonth());
		Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(),ar.get(0).getFuelTypeStatistics().size());
		Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(),ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
		Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(),ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

		Assertions.assertEquals(er.size(),ar.size());
		Assertions.assertEquals(er.get(1).getMonth(),ar.get(1).getMonth());
		Assertions.assertEquals(er.get(1).getFuelTypeStatistics().size(),ar.get(1).getFuelTypeStatistics().size());
		Assertions.assertEquals(er.get(1).getFuelTypeStatistics().get(0).getAveragePrice(),ar.get(1).getFuelTypeStatistics().get(0).getAveragePrice());
		Assertions.assertEquals(er.get(1).getFuelTypeStatistics().get(0).getTotalPrice(),ar.get(1).getFuelTypeStatistics().get(0).getTotalPrice());

		Assertions.assertEquals(er.size(),ar.size());
		Assertions.assertEquals(er.get(2).getMonth(),ar.get(2).getMonth());
		Assertions.assertEquals(er.get(2).getFuelTypeStatistics().size(),ar.get(2).getFuelTypeStatistics().size());
		Assertions.assertEquals(er.get(2).getFuelTypeStatistics().get(0).getAveragePrice(),ar.get(2).getFuelTypeStatistics().get(0).getAveragePrice());
		Assertions.assertEquals(er.get(2).getFuelTypeStatistics().get(0).getTotalPrice(),ar.get(2).getFuelTypeStatistics().get(0).getTotalPrice());


	}
	@Test
	void findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType_success_case() throws IOException {
		long driverId=3;
		List<FuelConsumptionPostRequest> fuelConsumptionDtoList= JsonUtil.getJsonListFromFile("file:src/test/resources/test-data/general_test_data.json");
		List<FuelConsumption> fuelConsumptionList = fuelConsumptionDtoList
				.stream()
				.filter(fuelConsumption -> fuelConsumption.getDriverId() == driverId)
				.map(FuelConsumptionPostRequest::toEntity)
				.collect(Collectors.toList());

		when(fuelConsumptionRepository.findByDriverId(anyLong())).thenReturn(fuelConsumptionList);

		List<FuelConsumptionStatisticResponse> er = fuelConsumptionService.findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(driverId);

		List<FuelConsumptionStatisticResponse> ar = getStatisticOfFuelConsumptionListByFuelTypeAndMonth(fuelConsumptionList);

		Assertions.assertEquals(er.size(),ar.size());
		Assertions.assertEquals(er.get(0).getMonth(),ar.get(0).getMonth());
		Assertions.assertEquals(er.get(0).getFuelTypeStatistics().size(),ar.get(0).getFuelTypeStatistics().size());
		Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getAveragePrice(),ar.get(0).getFuelTypeStatistics().get(0).getAveragePrice());
		Assertions.assertEquals(er.get(0).getFuelTypeStatistics().get(0).getTotalPrice(),ar.get(0).getFuelTypeStatistics().get(0).getTotalPrice());

		Assertions.assertEquals(er.size(),ar.size());
		Assertions.assertEquals(er.get(1).getMonth(),ar.get(1).getMonth());
		Assertions.assertEquals(er.get(1).getFuelTypeStatistics().size(),ar.get(1).getFuelTypeStatistics().size());
		Assertions.assertEquals(er.get(1).getFuelTypeStatistics().get(0).getAveragePrice(),ar.get(1).getFuelTypeStatistics().get(0).getAveragePrice());
		Assertions.assertEquals(er.get(1).getFuelTypeStatistics().get(0).getTotalPrice(),ar.get(1).getFuelTypeStatistics().get(0).getTotalPrice());

		Assertions.assertEquals(er.size(),ar.size());
		Assertions.assertEquals(er.get(2).getMonth(),ar.get(2).getMonth());
		Assertions.assertEquals(er.get(2).getFuelTypeStatistics().size(),ar.get(2).getFuelTypeStatistics().size());
		Assertions.assertEquals(er.get(2).getFuelTypeStatistics().get(0).getAveragePrice(),ar.get(2).getFuelTypeStatistics().get(0).getAveragePrice());
		Assertions.assertEquals(er.get(2).getFuelTypeStatistics().get(0).getTotalPrice(),ar.get(2).getFuelTypeStatistics().get(0).getTotalPrice());

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

}