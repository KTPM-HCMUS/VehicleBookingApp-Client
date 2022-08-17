package com.ktpm.vehiclebooking.model;


import java.util.Date;

public class User {
    private String userId;
    private String password;
    private String username;
    private String birthDate;
    private String email;
    private int role;
    private int transportationType;
    private String vehiclePlateNumber;


    public User() {
    }

    public User(String userid, String password, String username, String birthDate, String email, int role, int transportationType, String vehiclePlateNumber){
        this.userId = userid;
        this.password = password;
        this.username = username;
        this.birthDate = birthDate;
        this.email = email;
        this.role = role;
        this.transportationType = transportationType;
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public User(String userid, String username, String birthDate, String email, int role, int transportationType, String vehiclePlateNumber){
        this.userId = userid;
        this.username = username;
        this.birthDate = birthDate;
        this.email = email;
        this.role = role;
        this.transportationType = transportationType;
        this.vehiclePlateNumber = vehiclePlateNumber;
    }
    public String getUserID() {
        return userId;
    }

    public void setUserID(String userid) {
        this.userId = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(int type) {
        this.transportationType = type;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehicleplatenumber) {
        this.vehiclePlateNumber = vehicleplatenumber;
    }

}
