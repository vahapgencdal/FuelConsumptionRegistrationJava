package com.fuel.consumption.api.v1;

import com.fuel.consumption.api.dto.*;
import com.fuel.consumption.exception.FileIsEmptyException;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@RestController
@RequestMapping("/api/v1")
@Validated
@Api(value = "Fuel Consumption Registration api")
public class FuelConsumptionApi {


    @Autowired
    private FuelConsumptionService fuelConsumptionService;


    @RequestMapping(value = "/fuel/consumptions/single",method = RequestMethod.POST)
    @ApiOperation(value = "Register Single Record of Fuel Consumption",
            notes = "Register Single Record of Fuel Consumption with JSON Object; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd")
    public ResponseEntity<Object> registerFuelConsumption(@RequestBody @Valid FuelConsumptionPostRequest fuelConsumption) {
        fuelConsumptionService.insert(fuelConsumption);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/fuel/consumptions/list",method = RequestMethod.POST)
    @ApiOperation(value = "Register list of Fuel Consumptions with JSON List",
            notes = "Register list of Fuel Consumptions with JSON List; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd ")
    public ResponseEntity<Object> registerFuelConsumptionList(@RequestBody @Valid @NotNull @NotEmpty List<FuelConsumptionPostRequest> fuelConsumptionList) {

        fuelConsumptionService.insertAll(fuelConsumptionList);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/fuel/consumptions/file",method = RequestMethod.POST)
    @ApiOperation(value = "Register list of Fuel Consumptions with JSON File",
            notes = "Register list of Fuel Consumptions with JSON File; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd ")
    public ResponseEntity<Object> registerFuelConsumptionListWithFile(@RequestPart("file") @Valid @NotNull MultipartFile file) throws IOException {

        String content = new String(file.getBytes(), StandardCharsets.UTF_8);

        List<FuelConsumptionPostRequest> fuelConsumptionList = JsonUtil.getJsonListFromString(content);

        fuelConsumptionService.insertAll(fuelConsumptionList);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/fuel/consumptions",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve  total spent amount of money grouped by month",
            notes = "Retrieve  total spent amount of money grouped by month")
    public ResponseEntity<List<TotalSpentAmountOfMoneyResponse>> totalSpentAmountOfMoneyGroupedByMonth() {

        List<TotalSpentAmountOfMoneyResponse> responseList = fuelConsumptionService.findTotalSpentAmountOfMoneyGroupedByMonth();

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT):
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value = "/fuel/consumptions/{driverId}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve  total spent amount of money by driverId and grouped by month",
            notes = "Retrieve  total spent amount of money by driverId and grouped by month")
    public ResponseEntity<List<TotalSpentAmountOfMoneyResponse>> totalSpentAmountOfMoneyGroupedByMonth(@PathVariable @Positive(message = "Driver Id have to be positive number") long driverId) {

         List<TotalSpentAmountOfMoneyResponse> responseList = fuelConsumptionService.findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(driverId);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT):
                new ResponseEntity<>(responseList, HttpStatus.OK);

    }

    @RequestMapping(value = "/fuel/consumptions/months/{monthId}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve list of fuel consumption records for specified month",
            notes = "Retrieve list of fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)")
    public ResponseEntity<List<FuelConsumptionRecordSpecifiedByMonthResponse>> findFuelConsumptionRecordsByMonth(@PathVariable
                                                                                                                     @Min(value = 1, message = "FuelPrice can't be less than 1")
                                                                                                                     @Max(value = 12, message = "FuelType can't be bigger than 12") int monthId) {
        List<FuelConsumptionRecordSpecifiedByMonthResponse> responseList = fuelConsumptionService.findFuelConsumptionRecordsByMonth(monthId);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT):
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value = "/fuel/consumptions/months/{monthId}/{driverId}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve List Fuel consumption records for specified month and driverId",
            notes = "Retrieve list fuel consumption records for specified month and driverId (each row should contain: fuel type, volume, date, price, total price, driver ID)")
    public ResponseEntity<List<FuelConsumptionRecordSpecifiedByMonthResponse>> findFuelConsumptionRecordsByMonthAndDriverId(@PathVariable
                                                                                                                                @Min(value = 1, message = "FuelPrice can't be less than 1")
                                                                                                                                @Max(value = 12, message = "FuelType can't be bigger than 12") int  monthId,
                                                                                                                            @PathVariable @Positive(message = "Driver Id have to be positive number") long driverId) {
         List<FuelConsumptionRecordSpecifiedByMonthResponse> responseList = fuelConsumptionService.findFuelConsumptionRecordsByMonthAndDriverId(monthId, driverId);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT):
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    @RequestMapping(value = "/fuel/consumptions/statistics",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve Statistics for each month, list fuel consumption records grouped by fuel type",
            notes = "Retrieve Statistics for each month, list fuel consumption records grouped by fuel type (each row should contain: fuel type, volume, average price, total price)")
    public ResponseEntity<List<FuelConsumptionStatisticResponse>> findEachMonthFuelConsumptionStatisticsGroupedByFuelType() {

        List<FuelConsumptionStatisticResponse> responseList = fuelConsumptionService.findEachMonthFuelConsumptionStatisticsGroupedByFuelType();

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT):
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value = "fuel/consumptions/statistics/{driverId}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve driver consumption Statistics for each month, list fuel consumption records grouped by fuel type",
            notes = "Retrieve driver consumption Statistics for each month, list fuel consumption records grouped by fuel type (each row should contain: fuel type, volume, average price, total price)")
    public ResponseEntity<List<FuelConsumptionStatisticResponse>> findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(@PathVariable @Positive(message = "Driver Id have to be positive number") long driverId) {

        List<FuelConsumptionStatisticResponse> responseList = fuelConsumptionService.findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(driverId);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT):
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
