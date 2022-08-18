package com.ktpm.vehiclebooking.model;

public class Booking {
    private String pickupPlaceAddress;
    private String dropOffPlaceAddress;
    private Double pickUpPlaceLatitude;
    private Double pickUpPlaceLongitude;
    private Double dropOffPlaceLatitude;
    private Double dropOffPlaceLongitude;
    private User user;
    private String distanceInKm;
    private String priceInVND;
    private String transportationType;
    private Boolean available;
    private Boolean arrived;
    private Boolean finished;

    public Booking() {
    }

    public Booking(String pickupPlaceAddress, String dropOffPlaceAddress, Double pickUpPlaceLatitude, Double pickUpPlaceLongitude, Double dropOffPlaceLatitude, Double dropOffPlaceLongitude, User user, String distanceInKm, String priceInVND, String transportationType, Boolean available, Boolean arrived, Boolean finished) {
        this.pickupPlaceAddress = pickupPlaceAddress;
        this.dropOffPlaceAddress = dropOffPlaceAddress;
        this.pickUpPlaceLatitude = pickUpPlaceLatitude;
        this.pickUpPlaceLongitude = pickUpPlaceLongitude;
        this.dropOffPlaceLatitude = dropOffPlaceLatitude;
        this.dropOffPlaceLongitude = dropOffPlaceLongitude;
        this.user = user;
        this.distanceInKm = distanceInKm;
        this.priceInVND = priceInVND;
        this.transportationType = transportationType;
        this.available = available;
        this.arrived = arrived;
        this.finished = finished;
    }

    public String getPickupPlaceAddress() {
        return pickupPlaceAddress;
    }

    public void setPickupPlaceAddress(String pickupPlaceAddress) {
        this.pickupPlaceAddress = pickupPlaceAddress;
    }

    public String getDropOffPlaceAddress() {
        return dropOffPlaceAddress;
    }

    public void setDropOffPlaceAddress(String dropOffPlaceAddress) {
        this.dropOffPlaceAddress = dropOffPlaceAddress;
    }

    public Double getPickUpPlaceLatitude() {
        return pickUpPlaceLatitude;
    }

    public void setPickUpPlaceLatitude(Double pickUpPlaceLatitude) {
        this.pickUpPlaceLatitude = pickUpPlaceLatitude;
    }

    public Double getPickUpPlaceLongitude() {
        return pickUpPlaceLongitude;
    }

    public void setPickUpPlaceLongitude(Double pickUpPlaceLongitude) {
        this.pickUpPlaceLongitude = pickUpPlaceLongitude;
    }

    public Double getDropOffPlaceLatitude() {
        return dropOffPlaceLatitude;
    }

    public void setDropOffPlaceLatitude(Double dropOffPlaceLatitude) {
        this.dropOffPlaceLatitude = dropOffPlaceLatitude;
    }

    public Double getDropOffPlaceLongitude() {
        return dropOffPlaceLongitude;
    }

    public void setDropOffPlaceLongitude(Double dropOffPlaceLongitude) {
        this.dropOffPlaceLongitude = dropOffPlaceLongitude;
    }

    public User getuser() {
        return user;
    }

    public void setuser(User user) {
        this.user = user;
    }

    public String getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(String distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public String getPriceInVND() {
        return priceInVND;
    }

    public void setPriceInVND(String priceInVND) {
        this.priceInVND = priceInVND;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getArrived() {
        return arrived;
    }

    public void setArrived(Boolean arrived) {
        this.arrived = arrived;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
