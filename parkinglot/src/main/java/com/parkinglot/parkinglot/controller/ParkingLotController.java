package com.parkinglot.parkinglot.controller;

import com.parkinglot.parkinglot.model.Vehicle;

public interface ParkingLotController {
    String createLot(int numberOfSlots);

    String parkVehicle(Vehicle vehicle);

    String leaveSlot(int slotNumber);

    String getStatus();

    String getVehiclesByColor(String color);

    String getRegistrationByVehicle(String registrationNumber);
}
