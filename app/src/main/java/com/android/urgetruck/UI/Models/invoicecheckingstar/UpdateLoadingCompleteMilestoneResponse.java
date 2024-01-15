package com.android.urgetruck.UI.Models.invoicecheckingstar;

import java.util.ArrayList;

public class UpdateLoadingCompleteMilestoneResponse {
    private int requestId;
    private String status;
    private String statusMessage;

    private String vrn;
    private String locationCode;
    private String locationName;
    private String milestoneCode;
    private String milestoneTransactionCode;
    private int jobMilestoneId;
    private int  elvId;
    private String errorMessage;
    private ArrayList<Product> product;

    public UpdateLoadingCompleteMilestoneResponse(int requestId, String status, String statusMessage, String vrn, String locationCode, String locationName, String milestoneCode, String milestoneTransactionCode, int jobMilestoneId, int elvId, String errorMessage, ArrayList<Product> product) {
        this.requestId = requestId;
        this.status = status;
        this.statusMessage = statusMessage;
        this.vrn = vrn;
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.milestoneCode = milestoneCode;
        this.milestoneTransactionCode = milestoneTransactionCode;
        this.jobMilestoneId = jobMilestoneId;
        this.elvId = elvId;
        this.errorMessage = errorMessage;
        this.product = product;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
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

    public int getJobMilestoneId() {
        return jobMilestoneId;
    }

    public void setJobMilestoneId(int jobMilestoneId) {
        this.jobMilestoneId = jobMilestoneId;
    }

    public int getElvId() {
        return elvId;
    }

    public void setElvId(int elvId) {
        this.elvId = elvId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }


/*  public UpdateLoadingCompleteMilestoneResponse(int requestId, String status, String statusMessage) {
        this.requestId = requestId;
        this.status = status;
        this.statusMessage = statusMessage;
    }*/

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
