package com.example.mobilehealthinformation.form;

public class Hospital2 {

    private String hospitalId;
    private String name;
    private String location;
    private String contactNo;
    private boolean isActive;
    private String password;

    public Hospital2(String hospitalId, String name, String location, String contactNo, boolean isActive, String password) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.location = location;
        this.contactNo = contactNo;
        this.isActive = isActive;
        this.password = password;
    }

    // Getters and Setters
    public String getHospitalId() { return hospitalId; }
    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
