package com.fh.smarthouse.energy;

public class SolarPanel implements EnergySource {
	private double maxOutput = 0;
	private double capacity;

	public SolarPanel(double maxOutput) {
		this.maxOutput = maxOutput;
	}

	@Override
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	@Override
	public double getCapacity() {
		return maxOutput;
	}

	@Override
	public String getSourceName() {
		return "Solar Panel";
	}
}
