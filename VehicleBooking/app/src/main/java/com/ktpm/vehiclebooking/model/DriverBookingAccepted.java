package com.ktpm.vehiclebooking.model;

public class DriverBookingAccepted {
    private String userId;
    private String name;
    private String vehiclePlate;
    private int typeOfVehicle;
    private Double longtitude;
    private Double latitude;

    public DriverBookingAccepted(){}

    public DriverBookingAccepted(String userId, String name, String vehiclePlate, int typeOfVehicle, Double longtitude, Double latitude){
        this.userId = userId;
        this.name = name;
        this.vehiclePlate = vehiclePlate;
        this.typeOfVehicle = typeOfVehicle;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public int getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public void setTypeOfVehicle(int typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
