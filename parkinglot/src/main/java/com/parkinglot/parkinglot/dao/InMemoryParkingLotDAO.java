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
        floors.clear(); // Clear existing data
        List<ParkingFloor> newFloors = List.of(new ParkingFloor(1, slots));
        floors.addAll(newFloors);
    }

    @Override
    public void parkVehicle(ParkingSlot slot, Vehicle vehicle) {
        Optional<ParkingSlot> existingSlot = getSlotByNumber(slot.getSlotNumber());
        existingSlot.ifPresent(s -> s.parkVehicle(vehicle));
    }

    @Override
    public void leaveSlot(int slotNumber) {
        Optional<ParkingSlot> existingSlot = getSlotByNumber(slotNumber);
        existingSlot.ifPresent(ParkingSlot::leaveVehicle);
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
        return new ArrayList<>(floors);
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
    public void clearAll() {
        floors.clear();
    }

    private Optional<ParkingSlot> getSlotByNumber(int slotNumber) {
        return floors.stream()
                .flatMap(floor -> floor.getSlots().stream())
                .filter(slot -> slot.getSlotNumber() == slotNumber)
                .findFirst();
    }
}
