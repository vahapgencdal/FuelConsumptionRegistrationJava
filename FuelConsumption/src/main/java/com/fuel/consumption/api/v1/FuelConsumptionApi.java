package com.fuel.consumption.api.v1;

import com.fuel.consumption.api.dto.*;
import com.fuel.consumption.model.entity.FuelConsumption;
import com.fuel.consumption.model.service.FuelConsumptionService;
import com.fuel.consumption.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@RestController
@RequestMapping("/api/v1")
@Api(value = "Fuel Consumption Registration api")
public class FuelConsumptionApi {


    @Autowired
    private FuelConsumptionService fuelConsumptionService;


    @RequestMapping(value = "/fuel/consumptions/single",method = RequestMethod.POST)
    @ApiOperation(value = "Register Single Record of Fuel Consumption",
            notes = "Register Single Record of Fuel Consumption with JSON Object; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd")
    public ResponseEntity<Object> registerFuelConsumption(@RequestBody FuelConsumptionPostRequest fuelConsumption) {
        //TODO: unexpected errors should handle
        //TODO: Data Validation should be done
        fuelConsumptionService.insert(fuelConsumption);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/fuel/consumptions/list",method = RequestMethod.POST)
    @ApiOperation(value = "Register list of Fuel Consumptions with JSON List",
            notes = "Register list of Fuel Consumptions with JSON List; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd ")
    public ResponseEntity<Object> registerFuelConsumptionList(@RequestBody List<FuelConsumptionPostRequest> fuelConsumptionList) {
        //TODO: unexpected errors should handle
        //TODO: Data Validation should be done
        fuelConsumptionService.insertAll(fuelConsumptionList);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/fuel/consumptions/file",method = RequestMethod.POST)
    @ApiOperation(value = "Register list of Fuel Consumptions with JSON File",
            notes = "Register list of Fuel Consumptions with JSON File; Fuel Types: DIESEL,P95,P98 and LPG. DateFormat: yyyy-MM-dd ")
    public ResponseEntity<Object> registerFuelConsumptionListWithFile(@RequestPart("file") @Valid @NotNull @NotBlank MultipartFile file) throws IOException {
        //TODO: unexpected errors should handle
        //TODO: Data Validation should be done
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);

        List<FuelConsumptionPostRequest> fuelConsumptionList = JsonUtil.getJsonListFromString(content);

        fuelConsumptionService.insertAll(fuelConsumptionList);

        return ResponseEntity.created(null).build();
    }

    @RequestMapping(value = "/fuel/consumptions",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve  total spent amount of money grouped by month",
            notes = "Retrieve  total spent amount of money grouped by month")
    public ResponseEntity<List<TotalSpentAmountOfMoneyResponse>> totalSpentAmountOfMoneyGroupedByMonth() {
        //TODO:if there is no record no record error should return
        return ResponseEntity.ok(fuelConsumptionService.findTotalSpentAmountOfMoneyGroupedByMonth());
    }

    @RequestMapping(value = "/fuel/consumptions/{driverId}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve  total spent amount of money by driverId and grouped by month",
            notes = "Retrieve  total spent amount of money by driverId and grouped by month")
    public ResponseEntity<List<TotalSpentAmountOfMoneyResponse>> totalSpentAmountOfMoneyGroupedByMonth(@PathVariable long driverId) {
        //TODO:if there is no record no record error should return

        return ResponseEntity.ok(fuelConsumptionService.findTotalSpentAmountOfMoneyByDriverIdAndGroupedByMonth(driverId));
    }

    @RequestMapping(value = "/fuel/consumptions/months/{monthId}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve list of fuel consumption records for specified month",
            notes = "Retrieve list of fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)")
    public ResponseEntity<List<FuelConsumptionRecordSpecifiedByMonthDto>> findFuelConsumptionRecordsByMonth(@PathVariable int monthId) {
        //TODO:if there is no record no record error should return
        //TODO:validation of monthId
        return ResponseEntity.ok(fuelConsumptionService.findFuelConsumptionRecordsByMonth(monthId));
    }

    @RequestMapping(value = "/fuel/consumptions/months/{monthId}/{driverId}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve List Fuel consumption records for specified month and driverId",
            notes = "Retrieve list fuel consumption records for specified month and driverId (each row should contain: fuel type, volume, date, price, total price, driver ID)")
    public ResponseEntity<List<FuelConsumptionRecordSpecifiedByMonthDto>> findFuelConsumptionRecordsByMonthAndDriverId(@PathVariable int monthId, @PathVariable long driverId) {
        //TODO:validation of monthId
        //TODO:if there is no record no record error should return
        return ResponseEntity.ok(fuelConsumptionService.findFuelConsumptionRecordsByMonthAndDriverId(monthId,driverId));
    }
    @RequestMapping(value = "/fuel/consumptions/statistics",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve Statistics for each month, list fuel consumption records grouped by fuel type",
            notes = "Retrieve Statistics for each month, list fuel consumption records grouped by fuel type (each row should contain: fuel type, volume, average price, total price)")
    public ResponseEntity<List<FuelConsumptionStatisticResponse>> findEachMonthFuelConsumptionStatisticsGroupedByFuelType() {
        //TODO:if there is no record no record error should return
        return ResponseEntity.ok(fuelConsumptionService.findEachMonthFuelConsumptionStatisticsGroupedByFuelType());
    }

    @RequestMapping(value = "fuel/consumptions/statistics/{driverId}",method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve driver consumption Statistics for each month, list fuel consumption records grouped by fuel type",
            notes = "Retrieve driver consumption Statistics for each month, list fuel consumption records grouped by fuel type (each row should contain: fuel type, volume, average price, total price)")
    public ResponseEntity<List<FuelConsumptionStatisticResponse>> findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(@PathVariable long driverId) {
        //TODO:if there is no record no record error should return
        return ResponseEntity.ok(fuelConsumptionService.findEachMonthFuelConsumptionStatisticsByDriverIdAndGroupedByFuelType(driverId));
    }
}
