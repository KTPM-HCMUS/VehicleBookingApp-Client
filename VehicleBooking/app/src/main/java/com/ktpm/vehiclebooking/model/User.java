package com.ktpm.vehiclebooking.model;


import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String userId;
    private String password;
    private String name;
    private String dob;
    private String email;
    private int role;
    private int type;
    private String vehicle_plate;


    public User() {
    }

    public User(String userid, String password, String name, String dob, String email, int role, int type, String vehicle_plate){
        this.userId = userid;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.role = role;
        this.type = type;
        this.vehicle_plate = vehicle_plate;
    }

    public User(String userid, String name, String dob, String email, int role, int type, String vehicle_plate){
        this.userId = userid;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.role = role;
        this.type = type;
        this.vehicle_plate = vehicle_plate;
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

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getdob() {
        return dob;
    }

    public void setdob(String dob) {
        this.dob = dob;
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

    public int gettype() {
        return type;
    }

    public void settype(int type) {
        this.type = type;
    }

    public String getvehicle_plate() {
        return vehicle_plate;
    }

    public void setvehicle_plate(String vehicle_plate) {
        this.vehicle_plate = vehicle_plate;
    }

}
