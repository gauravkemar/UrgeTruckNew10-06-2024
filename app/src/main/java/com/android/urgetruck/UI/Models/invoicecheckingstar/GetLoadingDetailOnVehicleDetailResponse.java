package com.android.urgetruck.UI.Models.invoicecheckingstar;

import java.util.List;

public class GetLoadingDetailOnVehicleDetailResponse {
    private int elvId;
    private Object errorMessage;
    private int jobMilestoneId;
    private String locationCode;
    private String locationName;
    private String milestoneCode;
    private String milestoneTransactionCode;
    private List<Product> product;
    private String status;
    private String vrn;
    public GetLoadingDetailOnVehicleDetailResponse(int elvId, Object errorMessage, int jobMilestoneId, String locationCode, String locationName, String milestoneCode, String milestoneTransactionCode, List<Product> product, String status, String vrn) {
        this.elvId = elvId;
        this.errorMessage = errorMessage;
        this.jobMilestoneId = jobMilestoneId;
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.milestoneCode = milestoneCode;
        this.milestoneTransactionCode = milestoneTransactionCode;
        this.product = product;
        this.status = status;
        this.vrn = vrn;
    }

    public int getElvId() {
        return elvId;
    }

    public void setElvId(int elvId) {
        this.elvId = elvId;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getJobMilestoneId() {
        return jobMilestoneId;
    }

    public void setJobMilestoneId(int jobMilestoneId) {
        this.jobMilestoneId = jobMilestoneId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getMilestoneCode() {
        return milestoneCode;
    }

    public void setMilestoneCode(String milestoneCode) {
        this.milestoneCode = milestoneCode;
    }

    public String getMilestoneTransactionCode() {
        return milestoneTransactionCode;
    }

    public void setMilestoneTransactionCode(String milestoneTransactionCode) {
        this.milestoneTransactionCode = milestoneTransactionCode;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }
}
