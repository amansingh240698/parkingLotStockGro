package com.parkinglot.parkinglot;

import com.parkinglot.parkinglot.dao.InMemoryParkingLotDAO;
import com.parkinglot.parkinglot.service.ParkingLotService;
import com.parkinglot.parkinglot.service.ParkingLotServiceImpl;
import com.parkinglot.parkinglot.controller.CLIController;
import com.parkinglot.parkinglot.model.Vehicle;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParkinglotApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ParkinglotApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// Initialize DAO, Service, and Controller
		InMemoryParkingLotDAO parkingLotDAO = new InMemoryParkingLotDAO();
		ParkingLotService parkingLotService = new ParkingLotServiceImpl(parkingLotDAO);
		CLIController cliController = new CLIController(parkingLotService);

		// Use try-with-resources to ensure Scanner is closed properly
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Parking Lot Application - Enter commands:");

			while (true) {
				String input = scanner.nextLine();
				String[] parts = input.split(" ", 2);
				String command = parts[0].toLowerCase();
				String argsPart = parts.length > 1 ? parts[1] : "";

				switch (command) {
					case "create":
						if (argsPart.startsWith("lot ")) {
							int numberOfSlots = Integer.parseInt(argsPart.split(" ")[1]);
							System.out.println(cliController.createLot(numberOfSlots));
						} else {
							System.out.println("Invalid create command");
						}
						break;
					case "park":
						String[] parkDetails = argsPart.split(" ", 2);
						String registrationNumber = parkDetails[0];
						String color = parkDetails[1];
						Vehicle vehicle = new Vehicle(registrationNumber, color);
						System.out.println(cliController.parkVehicle(vehicle));
						break;
					case "leave":
						int slotNumber = Integer.parseInt(argsPart.split(" ")[1]);
						System.out.println(cliController.leaveSlot(slotNumber));
						break;
					case "status":
						System.out.println(cliController.getStatus());
						break;
					case "color":
						System.out.println(cliController.getVehiclesByColor(argsPart));
						break;
					case "registration":
						System.out.println(cliController.getRegistrationByVehicle(argsPart));
						break;
					case "exit":
						System.out.println("Exiting...");
						return;
					default:
						System.out.println("Invalid command");
				}
			}
		}
	}
}
