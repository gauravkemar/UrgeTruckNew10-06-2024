package com.android.urgetruck.UI.Models.exitclearancenew;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail {

    @SerializedName("productVerificationId")
    @Expose
    public Integer productVerificationId;
    @SerializedName("batchNumber")
    @Expose
    public String batchNumber;
    @SerializedName("vehicleTransactionId")
    @Expose
    public Integer vehicleTransactionId;
    @SerializedName("jobMilestoneId")
    @Expose
    public Integer jobMilestoneId;
    @SerializedName("isVerified")
    @Expose
    public Boolean isVerified;
    @SerializedName("verifiedBy")
    @Expose
    public Object verifiedBy;

    public ProductDetail(Integer productVerificationId, String batchNumber, Integer vehicleTransactionId, Integer jobMilestoneId, Boolean isVerified, Object verifiedBy) {
        super();
        this.productVerificationId = productVerificationId;
        this.batchNumber = batchNumber;
        this.vehicleTransactionId = vehicleTransactionId;
        this.jobMilestoneId = jobMilestoneId;
        this.isVerified = isVerified;
        this.verifiedBy = verifiedBy;
    }

    public Integer getProductVerificationId() {
        return productVerificationId;
    }

    public void setProductVerificationId(Integer productVerificationId) {
        this.productVerificationId = productVerificationId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getVehicleTransactionId() {
        return vehicleTransactionId;
    }

    public void setVehicleTransactionId(Integer vehicleTransactionId) {
        this.vehicleTransactionId = vehicleTransactionId;
    }

    public Integer getJobMilestoneId() {
        return jobMilestoneId;
    }

    public void setJobMilestoneId(Integer jobMilestoneId) {
        this.jobMilestoneId = jobMilestoneId;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Object getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Object verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
    public void updateFields( boolean newIsVerified) {
        this.isVerified = newIsVerified;
    }


}