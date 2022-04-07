package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExitClearanceModel {
    @SerializedName("vrn")
    @Expose
    private String vrn;
    @SerializedName("driverName")
    @Expose
    private String driverName;
    @SerializedName("vehicleTransactionId")
    @Expose
    private Integer vehicleTransactionId;
    @SerializedName("jobMilestoneId")
    @Expose
    private Integer jobMilestoneId;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
