
package com.parkinglot.parkinglot.dao;

import com.parkinglot.parkinglot.model.ParkingFloor;
import com.parkinglot.parkinglot.model.ParkingSlot;
import com.parkinglot.parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

@Repository

public class InMemoryParkingLotDAO implements ParkingLotDAO {
    private List<ParkingFloor> floors = new ArrayList<>();

    @Override
    public void createLot(List<ParkingSlot> slots) {
        // Assuming slots are distributed across floors.
        // Here, you may need to modify this method to handle floors properly.
        // For simplicity, you might create a single floor with all slots.
        ParkingFloor floor = new ParkingFloor(1, slots);
        this.floors.add(floor);
    }

    @Override
    public String parkVehicle(ParkingSlot slot, Vehicle vehicle) {
        if (slot.isOccupied()) {
            return "Slot already occupied";
        }
        slot.setVehicle(vehicle);
        return "Parked at slot " + slot.getSlotNumber();
    }

    @Override
    public String leaveSlot(int slotNumber) {
        for (ParkingFloor floor : floors) {
            Optional<ParkingSlot> slotOpt = floor.getSlots().stream()
                    .filter(slot -> slot.getSlotNumber() == slotNumber)
                    .findFirst();
            if (slotOpt.isPresent()) {
                ParkingSlot slot = slotOpt.get();
                if (!slot.isOccupied()) {
                    return "Slot is already empty";
                }
                Vehicle vehicle = slot.getVehicle();
                slot.setVehicle(null); // Set the vehicle to null to mark the slot as empty
                return "Leave " + vehicle.getRegistrationNumber();
            }
        }
        return "Invalid slot number";
    }

    @Override
    public String getStatus() {
        return floors.stream()
                .flatMap(floor -> floor.getSlots().stream()
                        .map(slot -> "Floor " + floor.getFloorNumber() + ", Slot " + slot.getSlotNumber() + " -> " +
                                (slot.isOccupied() ? slot.getVehicle().getRegistrationNumber() + " Car parked"
                                        : "Empty")))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<String> getVehiclesByColor(String color) {
        return floors.stream()
                .flatMap(floor -> floor.getSlots().stream())
                .filter(slot -> slot.isOccupied() && slot.getVehicle().getColor().equalsIgnoreCase(color))
                .map(slot -> slot.getVehicle().getRegistrationNumber())
                .collect(Collectors.toList());
    }

    @Override
    public String getRegistrationByVehicle(String registrationNumber) {
        return floors.stream()
                .flatMap(floor -> floor.getSlots().stream())
                .filter(slot -> slot.isOccupied()
                        && slot.getVehicle().getRegistrationNumber().equalsIgnoreCase(registrationNumber))
                .map(slot -> "Vehicle " + registrationNumber + " is parked at slot " + slot.getSlotNumber())
                .findFirst()
                .orElse("Vehicle not found");
    }

    @Override
    public ParkingSlot findNearestAvailableSlot() {
        return floors.stream()
                .flatMap(floor -> floor.getSlots().stream())
                .filter(slot -> !slot.isOccupied())
                .findFirst()
                .orElse(null);
    }

    @Override
    public ParkingSlot getSlot(int floorNumber, int slotNumber) {
        return floors.stream()
                .filter(floor -> floor.getFloorNumber() == floorNumber)
                .flatMap(floor -> floor.getSlots().stream())
                .filter(slot -> slot.getSlotNumber() == slotNumber)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ParkingFloor> getAllFloors() {
        return floors;
    }
}
