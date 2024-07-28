package com.parkinglot.parkinglot.dao;

import com.parkinglot.parkinglot.model.ParkingFloor;
import com.parkinglot.parkinglot.model.ParkingSlot;
import com.parkinglot.parkinglot.model.Vehicle;

import java.util.List;

public interface ParkingLotDAO {
    void createLot(List<ParkingSlot> slots);

    void parkVehicle(ParkingSlot slot, Vehicle vehicle);

    void leaveSlot(int slotNumber);

    ParkingSlot findNearestAvailableSlot();

    ParkingSlot getSlot(int floorNumber, int slotNumber);

    List<ParkingFloor> getAllFloors();

    String getStatus();

    void clearAll();
}
