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

    private final ParkingLotDAO parkingLotDAO;
    private List<Vehicle> parkedVehicles = new ArrayList<>();
    private boolean lotCreated = false;

    @Autowired
    public ParkingLotServiceImpl(ParkingLotDAO parkingLotDAO) {
        this.parkingLotDAO = parkingLotDAO;
    }

    @Override
    public String createLot(List<ParkingFloor> floors) {
        if (lotCreated) {
            return "A parking lot is already created. Please reset before creating a new lot.";
        }
        List<ParkingSlot> allSlots = floors.stream()
                .flatMap(floor -> floor.getSlots().stream())
                .collect(Collectors.toList());
        parkingLotDAO.createLot(allSlots);
        lotCreated = true;
        return "Parking lot created";
    }

    @Override
    public String parkVehicle(Vehicle vehicle) {
        if (!lotCreated) {
            return "No parking lot exists. Please create a lot first.";
        }
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
        if (!lotCreated) {
            return "No parking lot exists. Please create a lot first.";
        }
        ParkingSlot slot = parkingLotDAO.getSlot(floorNumber, slotNumber);
        if (slot != null && slot.isOccupied()) {
            Vehicle vehicle = slot.getVehicle();
            parkingLotDAO.leaveSlot(slotNumber);
            slot.leaveVehicle();
            parkedVehicles.remove(vehicle);
            return "Leave " + vehicle.getRegistrationNumber();
        }
        return "Slot is already empty";
    }

    @Override
    public String getStatus() {
        if (!lotCreated) {
            return "No parking lot exists.";
        }
        return parkingLotDAO.getAllFloors().stream()
                .flatMap(floor -> floor.getSlots().stream()
                        .map(slot -> "Floor " + floor.getFloorNumber() + ", Slot " + slot.getSlotNumber() + " -> " +
                                (slot.isOccupied() ? slot.getVehicle().getRegistrationNumber() + " Car parked"
                                        : "Empty")))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getVehiclesByColor(String color) {
        if (!lotCreated) {
            return "No parking lot exists.";
        }
        return parkedVehicles.stream()
                .filter(vehicle -> vehicle.getColor().equalsIgnoreCase(color))
                .map(Vehicle::getRegistrationNumber)
                .collect(Collectors.joining(", ", "Vehicles with color " + color + ": ", ""));
    }

    @Override
    public String getRegistrationByVehicle(String registrationNumber) {
        if (!lotCreated) {
            return "No parking lot exists.";
        }
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
    public boolean isLotCreated() {
        return lotCreated;
    }

    @Override
    public String resetLot() {
        if (!lotCreated) {
            return "No parking lot to reset.";
        }
        parkingLotDAO.clearAll(); // Clear all data in DAO (implement this method in your DAO if needed)
        parkedVehicles.clear();
        lotCreated = false;
        return "Parking lot has been reset.";
    }
}
