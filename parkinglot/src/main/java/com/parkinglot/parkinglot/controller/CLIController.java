package com.parkinglot.parkinglot.controller;

import com.parkinglot.parkinglot.model.ParkingFloor;
import com.parkinglot.parkinglot.model.ParkingSlot;
import com.parkinglot.parkinglot.model.Vehicle;
import com.parkinglot.parkinglot.service.ParkingLotService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@Component
public class CLIController implements ParkingLotController {

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    private final ParkingLotService parkingLotService;

    public CLIController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @Override
    public String createLot(int numberOfSlots) {
        List<ParkingSlot> slots = new ArrayList<>();
        for (int i = 1; i <= numberOfSlots; i++) {
            slots.add(new ParkingSlot(i));
        }
        List<ParkingFloor> floors = List.of(new ParkingFloor(1, slots));
        return parkingLotService.createLot(floors);
    }

    @Override
    public String parkVehicle(Vehicle vehicle) {
        return parkingLotService.parkVehicle(vehicle);
    }

    @Override
    public String leaveSlot(int slotNumber) {
        return parkingLotService.leaveSlot(1, slotNumber); // Assuming single floor for simplicity
    }

    @Override
    public String getStatus() {
        return parkingLotService.getStatus();
    }

    @Override
    public String getVehiclesByColor(String color) {
        return parkingLotService.getVehiclesByColor(color);
    }

    @Override
    public String getRegistrationByVehicle(String registrationNumber) {
        return parkingLotService.getRegistrationByVehicle(registrationNumber);
    }

    // Method to handle commands
    public void handleCommands() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Parking Lot Application - Enter commands:");

            while (true) {
                String input = scanner.nextLine();
                String response = handleCommand(input);
                System.out.println(response);
                if ("Exiting...".equals(response)) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while processing commands: " + e.getMessage());
        }
    }

    private String handleCommand(String command) {
        String[] parts = command.split(" ", 2);
        String action = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        try {
            switch (action) {
                case "create":
                    if (args.startsWith("lot ")) {
                        try {
                            int numberOfSlots = Integer.parseInt(args.split(" ")[1]);
                            return createLot(numberOfSlots);
                        } catch (NumberFormatException e) {
                            return "Invalid number of slots. Please enter a valid integer.";
                        }
                    }
                    return "Invalid create command format. Use 'create lot <number_of_slots>'.";
                case "park":
                    String[] parkDetails = args.split(" ", 2);
                    if (parkDetails.length == 2) {
                        String registrationNumber = parkDetails[0];
                        String color = parkDetails[1];

                        // Validate inputs
                        if (!isAlphanumeric(registrationNumber) || !isAlphanumeric(color)) {
                            return "Invalid input. Registration number and color must be alphanumeric without special characters.";
                        }

                        Vehicle vehicle = new Vehicle(registrationNumber, color);
                        return parkVehicle(vehicle);
                    }
                    return "Invalid park command format. Use 'park <registration_number> <color>'.";
                case "leave":
                    try {
                        int slotNumber = Integer.parseInt(args.split(" ")[1]);
                        return leaveSlot(slotNumber);
                    } catch (NumberFormatException e) {
                        return "Invalid slot number. Please enter a valid integer.";
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return "Slot number is missing. Use 'leave slot <slot_number>'.";
                    }
                case "status":
                    return getStatus();
                case "color":
                    return getVehiclesByColor(args);
                case "registration":
                    return getRegistrationByVehicle(args);
                case "reset":
                    return parkingLotService.resetLot();
                case "exit":
                    return "Exiting...";
                default:
                    return "Invalid command. Please enter a valid command.";
            }
        } catch (Exception e) {
            return "An error occurred while processing the command: " + e.getMessage();
        }
    }

    private boolean isAlphanumeric(String str) {
        return ALPHANUMERIC_PATTERN.matcher(str).matches();
    }
}
