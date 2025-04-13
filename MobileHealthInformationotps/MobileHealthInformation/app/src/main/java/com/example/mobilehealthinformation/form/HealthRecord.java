package com.example.mobilehealthinformation.form;

public class HealthRecord {
    private String recordId;
    private String medicine;
    private String treatment;
    private String doctorName;
    private String claimrequestid;

    public String getClaimrequestid() {
        return claimrequestid;
    }

    public void setClaimrequestid(String claimrequestid) {
        this.claimrequestid = claimrequestid;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    // Getters and Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
}
