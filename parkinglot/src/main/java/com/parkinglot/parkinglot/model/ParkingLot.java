package com.parkinglot.parkinglot.model;

import java.util.List;

public class ParkingLot {
    private List<ParkingFloor> floors;

    public ParkingLot(List<ParkingFloor> floors) {
        this.floors = floors;
    }

    public List<ParkingFloor> getFloors() {
        return floors;
    }

    public ParkingSlot findNearestAvailableSlot() {
        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                if (slot.isEmpty()) {
                    return slot;
                }
            }
        }
        return null;
    }

    public ParkingSlot findNearestAvailableSlot(int floorNumber) {
        ParkingFloor floor = getFloors().stream()
                .filter(f -> f.getFloorNumber() == floorNumber)
                .findFirst()
                .orElse(null);
        if (floor != null) {
            return floor.getSlots().stream()
                    .filter(ParkingSlot::isEmpty)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
