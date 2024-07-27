package com.parkinglot.parkinglot.controller;

import com.parkinglot.parkinglot.model.ParkingFloor;
import com.parkinglot.parkinglot.model.ParkingSlot;
import com.parkinglot.parkinglot.model.Vehicle;
import com.parkinglot.parkinglot.service.ParkingLotService;

import java.util.ArrayList;
import java.util.List;

public class CLIController implements ParkingLotController {
    private ParkingLotService parkingLotService;

    public CLIController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @Override
    public String createLot(int numberOfSlots) {
        List<ParkingSlot> slots = new ArrayList<>();
        for (int i = 1; i <= numberOfSlots; i++) {
            slots.add(new ParkingSlot(i));
        }
        List<ParkingFloor> floors = List.of(new ParkingFloor(1, slots));
        parkingLotService.createLot(floors);
        return "Parking lot created";
    }

    @Override
    public String parkVehicle(Vehicle vehicle) {
        return parkingLotService.parkVehicle(vehicle);
    }

    @Override
    public String leaveSlot(int slotNumber) {
        return parkingLotService.leaveSlot(1, slotNumber); // Assuming single floor for simplicity
    }

    @Override
    public String getStatus() {
        return parkingLotService.getStatus();
    }

    @Override
    public String getVehiclesByColor(String color) {
        return parkingLotService.getVehiclesByColor(color);
    }

    @Override
    public String getRegistrationByVehicle(String registrationNumber) {
        return parkingLotService.getRegistrationByVehicle(registrationNumber);
    }
}
