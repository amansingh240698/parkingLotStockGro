package com.parkinglot.parkinglot.service;

import com.parkinglot.parkinglot.dao.ParkingLotDAO;
import com.parkinglot.parkinglot.model.ParkingFloor;
import com.parkinglot.parkinglot.model.ParkingSlot;
import com.parkinglot.parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    private ParkingLotDAO parkingLotDAO;
    private List<Vehicle> parkedVehicles = new ArrayList<>();

    @Autowired
    public ParkingLotServiceImpl(ParkingLotDAO parkingLotDAO) {
        this.parkingLotDAO = parkingLotDAO;
    }

    @Override
    public String createLot(List<ParkingFloor> floors) {
        List<ParkingSlot> allSlots = floors.stream()
                .flatMap(floor -> floor.getSlots().stream())
                .collect(Collectors.toList());
        parkingLotDAO.createLot(allSlots);
        return "Parking lot created";
    }

    @Override
    public String parkVehicle(Vehicle vehicle) {
        ParkingSlot availableSlot = parkingLotDAO.findNearestAvailableSlot();
        if (availableSlot != null) {
            parkingLotDAO.parkVehicle(availableSlot, vehicle);
            parkedVehicles.add(vehicle);
            return "Parked at slot " + availableSlot.getSlotNumber();
        }
        return "Parking lot is full";
    }

    @Override
    public String leaveSlot(int floorNumber, int slotNumber) {
        ParkingSlot slot = parkingLotDAO.getSlot(floorNumber, slotNumber);
        if (slot != null && slot.isOccupied()) {
            Vehicle vehicle = slot.getVehicle();
            parkingLotDAO.leaveSlot(slotNumber);
            slot.leaveVehicle(); // Clear the slot
            parkedVehicles.remove(vehicle);
            return "Leave " + vehicle.getRegistrationNumber();
        }
        return "Slot is already empty";
    }

    @Override
    public String getStatus() {
        return parkingLotDAO.getAllFloors().stream()
                .flatMap(floor -> floor.getSlots().stream()
                        .map(slot -> "Floor " + floor.getFloorNumber() + ", Slot " + slot.getSlotNumber() + " -> " +
                                (slot.isOccupied() ? slot.getVehicle().getRegistrationNumber() + " Car parked"
                                        : "Empty")))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getVehiclesByColor(String color) {
        return parkedVehicles.stream()
                .filter(vehicle -> vehicle.getColor().equalsIgnoreCase(color))
                .map(Vehicle::getRegistrationNumber)
                .collect(Collectors.joining(", ", "Vehicles with color " + color + ": ", ""));
    }

    @Override
    public String getRegistrationByVehicle(String registrationNumber) {
        // Iterate through all floors and slots to find the vehicle
        for (ParkingFloor floor : parkingLotDAO.getAllFloors()) {
            for (ParkingSlot slot : floor.getSlots()) {
                if (slot.isOccupied()
                        && slot.getVehicle().getRegistrationNumber().equalsIgnoreCase(registrationNumber)) {
                    return "Vehicle " + registrationNumber + " is parked at slot " + slot.getSlotNumber() + " on floor "
                            + floor.getFloorNumber();
                }
            }
        }
        return "Vehicle not found";
    }

    @Override
    public String getSlotInfoByVehicle(String registrationNumber) {
        return parkingLotDAO.getAllFloors().stream()
                .flatMap(floor -> floor.getSlots().stream()
                        .filter(slot -> slot.isOccupied()
                                && slot.getVehicle().getRegistrationNumber().equalsIgnoreCase(registrationNumber))
                        .map(slot -> "Vehicle " + registrationNumber + " is parked at slot " + slot.getSlotNumber()))
                .findFirst()
                .orElse("Vehicle not found");
    }
}
