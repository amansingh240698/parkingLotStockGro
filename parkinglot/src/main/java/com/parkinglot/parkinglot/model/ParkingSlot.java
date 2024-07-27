package com.parkinglot.parkinglot.model;

public class ParkingSlot {
    private int slotNumber;
    private boolean occupied;
    private Vehicle vehicle;

    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.occupied = false;
        this.vehicle = null;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void parkVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.occupied = true;
    }

    public void leaveVehicle() {
        this.vehicle = null;
        this.occupied = false;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.occupied = (vehicle != null);
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Unimplemented method 'isEmpty'");
    }
}
