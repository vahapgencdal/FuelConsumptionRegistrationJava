package com.fuel.consumption.api.v1;

import com.fuel.consumption.api.dto.FuelConsumptionDto;
import com.fuel.consumption.model.service.FuelConsumptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(value = "Fuel Consumption Types api")
public class FuelTypesApi {


    @Autowired
    private FuelConsumptionService fuelConsumptionService;

    @RequestMapping(value = "/fuel/types",method = RequestMethod.GET)
    @ApiOperation(value = "Fuel Types",
            notes = "Retrieve All Fuel Types")
    public ResponseEntity<List<FuelConsumptionDto>> retrieveFuelTypes() {
        return ResponseEntity.ok(fuelConsumptionService.findAll());
    }

}
