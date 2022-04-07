
package com.android.urgetruck.UI.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobMilestone {

    @SerializedName("jobMilestoneId")
    @Expose
    private Integer jobMilestoneId;
    @SerializedName("elvId")
    @Expose
    private Integer elvId;
    @SerializedName("vehicleTransactionId")
    @Expose
    private Integer vehicleTransactionId;
    @SerializedName("milestoneTransactionCode")
    @Expose
    private String milestoneTransactionCode;
    @SerializedName("milestone")
    @Expose
    private String milestone;
    @SerializedName("milestoneCode")
    @Expose
    private String milestoneCode;
    @SerializedName("milestoneDescription")
    @Expose
    private Object milestoneDescription;
    @SerializedName("milestioneEvent")
    @Expose
    private String milestioneEvent;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("milestoneSequence")
    @Expose
    private Integer milestoneSequence;
    @SerializedName("isRequiredMilestone")
    @Expose
    private Boolean isRequiredMilestone;
    @SerializedName("isActiveMilestone")
    @Expose
    private Boolean isActiveMilestone;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("remarks")
    @Expose
    private Object remarks;
    @SerializedName("milestoneBeginTime")
    @Expose
    private Object milestoneBeginTime;
    @SerializedName("milestoneCompletionTime")
    @Expose
    private Object milestoneCompletionTime;
    @SerializedName("isAX4Updated")
    @Expose
    private Boolean isAX4Updated;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("jobMilestoneDetails")
    @Expose
    private List<Object> jobMilestoneDetails = null;
    @SerializedName("milestoneActionsTracking")
    @Expose
    private List<MilestoneActionsTracking> milestoneActionsTracking = null;
    @SerializedName("weighBridgeTransaction")
    @Expose
    private List<WeighBridgeTransaction> weighBridgeTransaction = null;
    @SerializedName("loadUnloadTransaction")
    @Expose
    private List<Object> loadUnloadTransaction = null;

    private boolean isExpandable;

    public JobMilestone() {
        this.isExpandable = isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public boolean isExpandable() {
        return isExpandable;
    }



    public Integer getJobMilestoneId() {
        return jobMilestoneId;
    }

    public void setJobMilestoneId(Integer jobMilestoneId) {
        this.jobMilestoneId = jobMilestoneId;
    }

    public Integer getElvId() {
        return elvId;
    }

    public void setElvId(Integer elvId) {
        this.elvId = elvId;
    }

    public Integer getVehicleTransactionId() {
        return vehicleTransactionId;
    }

    public void setVehicleTransactionId(Integer vehicleTransactionId) {
        this.vehicleTransactionId = vehicleTransactionId;
    }

    public String getMilestoneTransactionCode() {
        return milestoneTransactionCode;
    }

    public void setMilestoneTransactionCode(String milestoneTransactionCode) {
        this.milestoneTransactionCode = milestoneTransactionCode;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getMilestoneCode() {
        return milestoneCode;
    }

    public void setMilestoneCode(String milestoneCode) {
        this.milestoneCode = milestoneCode;
    }

    public Object getMilestoneDescription() {
        return milestoneDescription;
    }

    public void setMilestoneDescription(Object milestoneDescription) {
        this.milestoneDescription = milestoneDescription;
    }

    public String getMilestioneEvent() {
        return milestioneEvent;
    }

    public void setMilestioneEvent(String milestioneEvent) {
        this.milestioneEvent = milestioneEvent;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public Integer getMilestoneSequence() {
        return milestoneSequence;
    }

    public void setMilestoneSequence(Integer milestoneSequence) {
        this.milestoneSequence = milestoneSequence;
    }

    public Boolean getIsRequiredMilestone() {
        return isRequiredMilestone;
    }

    public void setIsRequiredMilestone(Boolean isRequiredMilestone) {
        this.isRequiredMilestone = isRequiredMilestone;
    }

    public Boolean getIsActiveMilestone() {
        return isActiveMilestone;
    }

    public void setIsActiveMilestone(Boolean isActiveMilestone) {
        this.isActiveMilestone = isActiveMilestone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public Object getMilestoneBeginTime() {
        return milestoneBeginTime;
    }

    public void setMilestoneBeginTime(Object milestoneBeginTime) {
        this.milestoneBeginTime = milestoneBeginTime;
    }

    public Object getMilestoneCompletionTime() {
        return milestoneCompletionTime;
    }

    public void setMilestoneCompletionTime(Object milestoneCompletionTime) {
        this.milestoneCompletionTime = milestoneCompletionTime;
    }

    public Boolean getIsAX4Updated() {
        return isAX4Updated;
    }

    public void setIsAX4Updated(Boolean isAX4Updated) {
        this.isAX4Updated = isAX4Updated;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public List<Object> getJobMilestoneDetails() {
        return jobMilestoneDetails;
    }

    public void setJobMilestoneDetails(List<Object> jobMilestoneDetails) {
        this.jobMilestoneDetails = jobMilestoneDetails;
    }

    public List<MilestoneActionsTracking> getMilestoneActionsTracking() {
        return milestoneActionsTracking;
    }

    public void setMilestoneActionsTracking(List<MilestoneActionsTracking> milestoneActionsTracking) {
        this.milestoneActionsTracking = milestoneActionsTracking;
    }

    public List<WeighBridgeTransaction> getWeighBridgeTransaction() {
        return weighBridgeTransaction;
    }

    public void setWeighBridgeTransaction(List<WeighBridgeTransaction> weighBridgeTransaction) {
        this.weighBridgeTransaction = weighBridgeTransaction;
    }

    public List<Object> getLoadUnloadTransaction() {
        return loadUnloadTransaction;
    }

    public void setLoadUnloadTransaction(List<Object> loadUnloadTransaction) {
        this.loadUnloadTransaction = loadUnloadTransaction;
    }

}
