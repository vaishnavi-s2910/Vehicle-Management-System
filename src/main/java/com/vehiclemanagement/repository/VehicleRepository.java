package com.vehiclemanagement.repository;

import com.vehiclemanagement.model.Vehicle;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class VehicleRepository {
    private final Path file = Paths.get("vehicles.csv");

    public VehicleRepository() {
        if (!Files.exists(file)) {
            List<Vehicle> sample = List.of(
                new Vehicle("TN01AB1234", "Swift", "Car", 2022, "Available", 1500),
                new Vehicle("TN02CD5678", "Activa", "Bike", 2023, "In Use", 500),
                new Vehicle("TN03EF9012", "Innova", "SUV", 2021, "Available", 2500)
            );
            saveAll(sample);
        }
    }

    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        if (!Files.exists(file)) return vehicles;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length == 6) {
                    vehicles.add(new Vehicle(p[0], p[1], p[2],
                        Integer.parseInt(p[3]), p[4], Double.parseDouble(p[5])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Could not read vehicles.csv", e);
        }
        return vehicles;
    }

    public void saveAll(List<Vehicle> vehicles) {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Vehicle v : vehicles) {
                writer.write(String.join(",", clean(v.getNumber()), clean(v.getName()),
                    clean(v.getType()), String.valueOf(v.getYear()), clean(v.getStatus()),
                    String.valueOf(v.getDailyRate())));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not save vehicles.csv", e);
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(",", " ").trim();
    }
}
