package com.fh.smarthouse.objects;

public class SmartObject {

	private String name;
	private double powerConsumption;
	private boolean isOn;

	public SmartObject(String name, double powerConsumption) {
		this.name = name;
		this.powerConsumption = powerConsumption;
		this.isOn = false;
	}

	public String getName() {
		return name;
	}

	public double getConsumption() {
		return isOn ? powerConsumption : 0;
	}

	public double getWatt() {
		return  powerConsumption;
	}
	public boolean isOn() {
		return isOn;
	}
	public void turnOn() {
		isOn = true;
	}
	public void turnOff() {
		isOn = false;
	}


	public void setName(String name) {
		this.name = name;
	}

	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
	}
}
