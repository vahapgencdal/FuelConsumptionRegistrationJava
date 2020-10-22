package com.fuel.consumption.api.v1;

import com.fuel.consumption.api.request.FuelReportPostRequest;
import com.fuel.consumption.api.response.ExpenseReportResponse;
import com.fuel.consumption.api.response.FuelReportMonthlyResponse;
import com.fuel.consumption.api.response.FuelReportResponse;
import com.fuel.consumption.api.validator.ValueOfEnum;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.GroupByType;
import com.fuel.consumption.util.JsonUtil;
import com.fuel.consumption.util.ReportPeriod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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


    @RequestMapping(value = "/fuel/expenses/single", method = RequestMethod.POST)
    @ApiOperation(value = "Register Single Record of Fuel Consumption",
            notes = "Register Single Record of Fuel Consumption with JSON Object; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd")
    public ResponseEntity<Object> registerFuelExpense(@RequestBody @Valid FuelReportPostRequest fuelConsumption) {
        fuelConsumptionService.insert(fuelConsumption);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/fuel/expenses/list", method = RequestMethod.POST)
    @ApiOperation(value = "Register list of Fuel Consumptions with JSON List",
            notes = "Register list of Fuel Consumptions with JSON List; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd ")
    public ResponseEntity<Object> registerFuelExpenseList(@RequestBody @Valid @NotNull @NotEmpty List<FuelReportPostRequest> fuelConsumptionList) {

        fuelConsumptionService.insertAll(fuelConsumptionList);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/fuel/expenses/file", method = RequestMethod.POST)
    @ApiOperation(value = "Register list of Fuel Consumptions with JSON File",
            notes = "Register list of Fuel Consumptions with JSON File; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd ")
    public ResponseEntity<Object> registerFuelExpenseListWithFile(@RequestPart("file") @Valid @NotNull MultipartFile file) throws IOException {

        String content = new String(file.getBytes(), StandardCharsets.UTF_8);

        List<FuelReportPostRequest> fuelConsumptionList = JsonUtil.getJsonListFromString(content);

        fuelConsumptionService.insertAll(fuelConsumptionList);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/report/expense", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve  total spent amount of money grouped by MONTH/YEAR",
            notes = "Retrieve  total spent amount of money grouped by MONTH/YEAR")
    public ResponseEntity<List<ExpenseReportResponse>> expenseReportByPeriod(
            @RequestParam @ValueOfEnum(enumClass = ReportPeriod.class, message = "Period Type should be MONTH or YEAR") String period) {

        List<ExpenseReportResponse> responseList = fuelConsumptionService.findExpenseReportByPeriod(period);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value = "/report/expense/{driverId}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve  total spent amount of money by driverId and grouped by MONTH/YEAR",
            notes = "Retrieve  total spent amount of money by driverId and grouped by MONTH/YEAR")
    public ResponseEntity<List<ExpenseReportResponse>> expenseReportByPeriodAndDriverId(
            @PathVariable @Positive(message = "Driver Id have to be positive number") long driverId,
            @RequestParam @ValueOfEnum(enumClass = ReportPeriod.class, message = "Period Type should be MONTH or YEAR") String period) {

        List<ExpenseReportResponse> responseList = fuelConsumptionService.findExpenseReportByPeriodAndDriverId(driverId, period);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(responseList, HttpStatus.OK);

    }

    @RequestMapping(value = "/report/fuel/{monthId}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve list of fuel consumption records for specified month",
            notes = "Retrieve list of fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)")
    public ResponseEntity<List<FuelReportResponse>> fuelReportByMonthId(@PathVariable
                                                                        @Min(value = 1, message = "FuelPrice can't be less than 1")
                                                                        @Max(value = 12, message = "FuelType can't be bigger than 12") int monthId) {
        List<FuelReportResponse> responseList = fuelConsumptionService.fuelReports(monthId);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value = "/report/fuel/{monthId}/{driverId}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve List Fuel consumption records for specified month and driverId",
            notes = "Retrieve list fuel consumption records for specified month and driverId (each row should contain: fuel type, volume, date, price, total price, driver ID)")
    public ResponseEntity<List<FuelReportResponse>> fuelReportByMonthIdAndDriverId(@PathVariable
                                                                                   @Min(value = 1, message = "FuelPrice can't be less than 1")
                                                                                   @Max(value = 12, message = "FuelType can't be bigger than 12") int monthId,
                                                                                   @PathVariable @Positive(message = "Driver Id have to be positive number") long driverId) {
        List<FuelReportResponse> responseList = fuelConsumptionService.fuelReports(monthId, driverId);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value = "/report/fuel/months", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve Statistics for each month, list fuel consumption records grouped by FUEL_TYPE",
            notes = "Retrieve Statistics for each month, list fuel consumption records grouped by FUEL_TYPE (each row should contain: fuel type, volume, average price, total price)")
    public ResponseEntity<List<FuelReportMonthlyResponse>> monthlyFuelReports(
            @RequestParam @ValueOfEnum(enumClass = GroupByType.class, message = "Group BY support only FUEL_TYPE for now ") String groupBy) {

        List<FuelReportMonthlyResponse> responseList = fuelConsumptionService.monthlyFuelReports(groupBy);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value = "/report/fuel/months/{driverId}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve driver consumption Statistics for each month, list fuel consumption records grouped by FUEL_TYPE",
            notes = "Retrieve driver consumption Statistics for each month, list fuel consumption records grouped by FUEL_TYPE (each row should contain: fuel type, volume, average price, total price)")
    public ResponseEntity<List<FuelReportMonthlyResponse>> monthlyFuelReportsByDriverId(
            @PathVariable @Positive(message = "Driver Id have to be positive number") long driverId,
            @RequestParam @ValueOfEnum(enumClass = GroupByType.class, message = "Group BY support only FUEL_TYPE for now") String groupBy) {

        List<FuelReportMonthlyResponse> responseList = fuelConsumptionService.monthlyFuelReports(driverId, groupBy);

        return CollectionUtils.isEmpty(responseList) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
