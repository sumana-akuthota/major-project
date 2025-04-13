package com.example.mobilehealthinformation.form;

public class Patient {
    private String aadhaarNumber;
    private String name;
    private int age;
    private String gender;
    private String contactNo;
    private String address;
    private String password;

    // Getters and Setters
    public String getAadhaarNo() {
        return aadhaarNumber;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNumber = aadhaarNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}



