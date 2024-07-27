package com.parkinglot.parkinglot.dao;

import com.parkinglot.parkinglot.model.ParkingSlot;
import com.parkinglot.parkinglot.model.ParkingFloor;
import com.parkinglot.parkinglot.model.Vehicle;

import java.util.List;

public interface ParkingLotDAO {
    void createLot(List<ParkingSlot> slots);

    String parkVehicle(ParkingSlot slot, Vehicle vehicle);

    String leaveSlot(int slotNumber);

    String getStatus();

    List<String> getVehiclesByColor(String color);

    String getRegistrationByVehicle(String registrationNumber);

    ParkingSlot findNearestAvailableSlot();

    ParkingSlot getSlot(int floorNumber, int slotNumber);

    List<ParkingFloor> getAllFloors();
}
