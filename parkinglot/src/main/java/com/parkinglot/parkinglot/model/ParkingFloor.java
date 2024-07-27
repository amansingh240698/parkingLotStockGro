package com.parkinglot.parkinglot.model;

import java.util.List;

public class ParkingFloor {
    private int floorNumber;
    private List<ParkingSlot> slots;

    public ParkingFloor(int floorNumber, List<ParkingSlot> slots) {
        this.floorNumber = floorNumber;
        this.slots = slots;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }
}
