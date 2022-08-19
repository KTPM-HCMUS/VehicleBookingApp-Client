package com.ktpm.vehiclebooking.model;

public class Booking {
    private String addressDepart;
    private String dropOffPlaceAddress;
    private Double latitudeDepart;
    private Double longitudeDepart;
    private Double latitudeDestination;
    private Double longitudeDestination;
    private int typeOfVehicle;
//    private String distanceInKm;
    private String priceInVND;

    public Booking() {
    }

    public Booking(String addressDepart, String dropOffPlaceAddress, Double latitudeDepart, Double longitudeDepart, Double latitudeDestination, Double longitudeDestination, int typeOfVehicle, String priceInVND) {
        this.addressDepart = addressDepart;
        this.dropOffPlaceAddress = dropOffPlaceAddress;
        this.latitudeDepart = latitudeDepart;
        this.longitudeDepart = longitudeDepart;
        this.latitudeDestination = latitudeDestination;
        this.longitudeDestination = longitudeDestination;
//        this.distanceInKm = distanceInKm;
        this.priceInVND = priceInVND;
        this.typeOfVehicle = typeOfVehicle;
    }

    public String getaddressDepart() {
        return addressDepart;
    }

    public void setaddressDepart(String addressDepart) {
        this.addressDepart = addressDepart;
    }

    public String getDropOffPlaceAddress() {
        return dropOffPlaceAddress;
    }

    public void setDropOffPlaceAddress(String dropOffPlaceAddress) {
        this.dropOffPlaceAddress = dropOffPlaceAddress;
    }

    public Double getlatitudeDepart() {
        return latitudeDepart;
    }

    public void setlatitudeDepart(Double latitudeDepart) {
        this.latitudeDepart = latitudeDepart;
    }

    public Double getlongitudeDepart() {
        return longitudeDepart;
    }

    public void setlongitudeDepart(Double longitudeDepart) {
        this.longitudeDepart = longitudeDepart;
    }

    public Double getlatitudeDestination() {
        return latitudeDestination;
    }

    public void setlatitudeDestination(Double latitudeDestination) {
        this.latitudeDestination = latitudeDestination;
    }

    public Double getlongitudeDestination() {
        return longitudeDestination;
    }

    public void setlongitudeDestination(Double longitudeDestination) {
        this.longitudeDestination = longitudeDestination;
    }

//    public String getDistanceInKm() {
//        return distanceInKm;
//    }
//
//    public void setDistanceInKm(String distanceInKm) {
//        this.distanceInKm = distanceInKm;
//    }
//
    public String getPriceInVND() {
        return priceInVND;
    }

    public void setPriceInVND(String priceInVND) {
        this.priceInVND = priceInVND;
    }

    public int gettypeOfVehicle() {
        return typeOfVehicle;
    }

    public void settypeOfVehicle(int typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }
}
