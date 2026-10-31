package com.vehiclemanagement.model;

public class Vehicle {
    private String number;
    private String name;
    private String type;
    private int year;
    private String status;
    private double dailyRate;

    public Vehicle(String number, String name, String type, int year, String status, double dailyRate) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.year = year;
        this.status = status;
        this.dailyRate = dailyRate;
    }

    public String getNumber() { return number; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getYear() { return year; }
    public String getStatus() { return status; }
    public double getDailyRate() { return dailyRate; }

    public void setNumber(String number) { this.number = number; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setYear(int year) { this.year = year; }
    public void setStatus(String status) { this.status = status; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }
}
