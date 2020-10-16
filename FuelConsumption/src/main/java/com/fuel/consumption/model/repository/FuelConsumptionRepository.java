package com.fuel.consumption.model.repository;

import com.fuel.consumption.model.entity.FuelConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface FuelConsumptionRepository extends JpaRepository<FuelConsumption, Long>,
        JpaSpecificationExecutor<FuelConsumption> {



}