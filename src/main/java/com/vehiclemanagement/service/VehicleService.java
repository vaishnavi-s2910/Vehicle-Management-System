package com.vehiclemanagement.service;

import com.vehiclemanagement.model.Vehicle;
import com.vehiclemanagement.repository.VehicleRepository;
import java.util.List;

public class VehicleService {
    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public List<Vehicle> getAll() { return repository.findAll(); }

    public void add(Vehicle vehicle) {
        List<Vehicle> list = getAll();
        if (vehicle.getNumber().isBlank() || vehicle.getName().isBlank())
            throw new IllegalArgumentException("Vehicle number and name are required.");
        if (list.stream().anyMatch(v -> v.getNumber().equalsIgnoreCase(vehicle.getNumber())))
            throw new IllegalArgumentException("That vehicle number already exists.");
        list.add(vehicle);
        repository.saveAll(list);
    }

    public void update(String oldNumber, Vehicle updated) {
        List<Vehicle> list = getAll();
        Vehicle existing = list.stream()
            .filter(v -> v.getNumber().equalsIgnoreCase(oldNumber)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found."));
        if (!oldNumber.equalsIgnoreCase(updated.getNumber()) &&
            list.stream().anyMatch(v -> v.getNumber().equalsIgnoreCase(updated.getNumber())))
            throw new IllegalArgumentException("That vehicle number already exists.");
        existing.setNumber(updated.getNumber());
        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setYear(updated.getYear());
        existing.setStatus(updated.getStatus());
        existing.setDailyRate(updated.getDailyRate());
        repository.saveAll(list);
    }

    public void delete(String number) {
        List<Vehicle> list = getAll();
        list.removeIf(v -> v.getNumber().equalsIgnoreCase(number));
        repository.saveAll(list);
    }
}
