package com.example.mobilehealthinformation.form;

public class ClaimRequest {

    private String claimId;
    private String patientAadhaarNo;
    private String policyId;
    private String hospitalId;
    private String description;
    private String status;
    private String documentUrl; // Added missing field

    // Getters and Setters
    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }

    public String getPatientAadhaarNo() { return patientAadhaarNo; }
    public void setPatientAadhaarNo(String patientAadhaarNo) { this.patientAadhaarNo = patientAadhaarNo; }

    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }

    public String getHospitalId() { return hospitalId; }
    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDocumentUrl() { return documentUrl; } // Fixed missing return statement
    public void setDocumentUrl(String documentUrl) { this.documentUrl = documentUrl; } // Added setter
}
