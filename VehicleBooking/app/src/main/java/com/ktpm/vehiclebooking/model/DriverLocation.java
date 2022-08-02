package com.ktpm.vehiclebooking.model;
/**
 * Data model for driver location
 */
public class DriverLocation {
    private Double currentPositionLatitude;
    private Double currentPositionLongitude;

    public DriverLocation(){

    }

    public DriverLocation(Double currentPositionLatitude, Double currentPositionLongitude) {
        this.currentPositionLatitude = currentPositionLatitude;
        this.currentPositionLongitude = currentPositionLongitude;
    }

    public Double getCurrentPositionLatitude() {
        return currentPositionLatitude;
    }

    public void setCurrentPositionLatitude(Double currentPositionLatitude) {
        this.currentPositionLatitude = currentPositionLatitude;
    }

    public Double getCurrentPositionLongitude() {
        return currentPositionLongitude;
    }

    public void setCurrentPositionLongitude(Double currentPositionLongitude) {
        this.currentPositionLongitude = currentPositionLongitude;
    }
}
