package com.mmi.intouch.dodointel;

public class wifi {
	private String macAddress;
	private int signalStrength;
	private int age;
	private int signalToNoiseRatio;
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public int getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getSignalToNoiseRatio() {
		return signalToNoiseRatio;
	}
	public void setSignalToNoiseRatio(int signalToNoiseRatio) {
		this.signalToNoiseRatio = signalToNoiseRatio;
	}
		
	
}
