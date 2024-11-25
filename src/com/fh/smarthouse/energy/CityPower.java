package com.fh.smarthouse.energy;

public class CityPower implements EnergySource {
    private final double powerLimit;
    private double capacity;
    public CityPower(double powerLimit) {
        this.powerLimit = powerLimit;
    }

    @Override
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public double getCapacity() {
        return powerLimit;
    }

    @Override
    public String getSourceName() {
        return "City Power";
    }

   }
