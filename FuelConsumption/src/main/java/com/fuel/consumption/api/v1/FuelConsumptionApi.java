package com.fuel.consumption.api.v1;

import com.fuel.consumption.api.dto.FuelConsumptionDto;
import com.fuel.consumption.model.service.FuelConsumptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(value = "Fuel Consumption Registration api")
public class FuelConsumptionApi {


    @Autowired
    private FuelConsumptionService fuelConsumptionService;

    @RequestMapping(value = "/fuel/consumptions",method = RequestMethod.GET)
    @ApiOperation(value = "Fuel Consumptions",
            notes = "Retrieve All Consumptions")
    public ResponseEntity<List<FuelConsumptionDto>> retrieveFuelConsumptions() {
        return ResponseEntity.ok(fuelConsumptionService.findAll());
    }

    @RequestMapping(value = "/fuel/consumptions/{driverId}",method = RequestMethod.GET)
    @ApiOperation(value = "Fuel Consumptions",
            notes = "Retrieve All Consumptions by Driver ID")
    public ResponseEntity<List<FuelConsumptionDto>> retrieveFuelConsumptionsByDriverId(@PathVariable long driverId) {
        return ResponseEntity.ok(fuelConsumptionService.findAllByDriverId(driverId));
    }

}
