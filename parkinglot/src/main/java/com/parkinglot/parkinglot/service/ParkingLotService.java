package com.parkinglot.parkinglot.service;

import com.parkinglot.parkinglot.model.ParkingFloor;
import com.parkinglot.parkinglot.model.Vehicle;

import java.util.List;

public interface ParkingLotService {

    String createLot(List<ParkingFloor> floors);

    String parkVehicle(Vehicle vehicle);

    String leaveSlot(int floorNumber, int slotNumber);

    String getStatus();

    String getVehiclesByColor(String color);

    String getRegistrationByVehicle(String registrationNumber);

    boolean isLotCreated();

    String resetLot();
}
