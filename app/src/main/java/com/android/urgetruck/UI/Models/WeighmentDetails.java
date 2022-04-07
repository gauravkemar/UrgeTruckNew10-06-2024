package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeighmentDetails {
    @SerializedName("vrn")
    @Expose
    private String vrn;
    @SerializedName("vehicleTransactionId")
    @Expose
    private Integer vehicleTransactionId;
    @SerializedName("jobMilestoneId")
    @Expose
    private Integer jobMilestoneId;
    @SerializedName("weighmentType")
    @Expose
    private String weighmentType;
    @SerializedName("expectedWeight")
    @Expose
    private String expectedWeight;
    @SerializedName("actualWeight")
    @Expose
    private String actualWeight;

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
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

    public String getWeighmentType() {
        return weighmentType;
    }

    public void setWeighmentType(String weighmentType) {
        this.weighmentType = weighmentType;
    }

    public String getExpectedWeight() {
        return expectedWeight;
    }

    public void setExpectedWeight(String expectedWeight) {
        this.expectedWeight = expectedWeight;
    }

    public String getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }
}
