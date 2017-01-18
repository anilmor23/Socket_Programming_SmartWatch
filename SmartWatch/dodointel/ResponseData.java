package com.mmi.intouch.dodointel;
public class ResponseData {
	private gps_data location;
	private Double accuracy;
	public gps_data getLocation() {
		return location;
	}
	public void setLocation(gps_data location) {
		this.location = location;
	}
	public Double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}
	
}
