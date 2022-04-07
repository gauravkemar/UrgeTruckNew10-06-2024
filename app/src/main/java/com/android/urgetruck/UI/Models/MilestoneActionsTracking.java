
package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MilestoneActionsTracking {

    @SerializedName("milestoneActionsTrackingId")
    @Expose
    private Integer milestoneActionsTrackingId;
    @SerializedName("jobMilestoneId")
    @Expose
    private Integer jobMilestoneId;
    @SerializedName("milestoneAction")
    @Expose
    private String milestoneAction;
    @SerializedName("actionCode")
    @Expose
    private String actionCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isRequired")
    @Expose
    private Boolean isRequired;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isDependent")
    @Expose
    private Boolean isDependent;
    @SerializedName("dependentActionId")
    @Expose
    private Boolean dependentActionId;
    @SerializedName("isDependentOnAllPrevious")
    @Expose
    private Boolean isDependentOnAllPrevious;
    @SerializedName("sequence")
    @Expose
    private Integer sequence;
    @SerializedName("deActivatedBy")
    @Expose
    private Object deActivatedBy;
    @SerializedName("remarks")
    @Expose
    private Object remarks;
    @SerializedName("isSelected")
    @Expose
    private Boolean isSelected;
    @SerializedName("isShown")
    @Expose
    private Boolean isShown;
    @SerializedName("isRemark")
    @Expose
    private Boolean isRemark;

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    @SerializedName("completionTime")
    @Expose
    private String completionTime;


    public Integer getMilestoneActionsTrackingId() {
        return milestoneActionsTrackingId;
    }

    public void setMilestoneActionsTrackingId(Integer milestoneActionsTrackingId) {
        this.milestoneActionsTrackingId = milestoneActionsTrackingId;
    }

    public Integer getJobMilestoneId() {
        return jobMilestoneId;
    }

    public void setJobMilestoneId(Integer jobMilestoneId) {
        this.jobMilestoneId = jobMilestoneId;
    }

    public String getMilestoneAction() {
        return milestoneAction;
    }

    public void setMilestoneAction(String milestoneAction) {
        this.milestoneAction = milestoneAction;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDependent() {
        return isDependent;
    }

    public void setIsDependent(Boolean isDependent) {
        this.isDependent = isDependent;
    }

    public Boolean getDependentActionId() {
        return dependentActionId;
    }

    public void setDependentActionId(Boolean dependentActionId) {
        this.dependentActionId = dependentActionId;
    }

    public Boolean getIsDependentOnAllPrevious() {
        return isDependentOnAllPrevious;
    }

    public void setIsDependentOnAllPrevious(Boolean isDependentOnAllPrevious) {
        this.isDependentOnAllPrevious = isDependentOnAllPrevious;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Object getDeActivatedBy() {
        return deActivatedBy;
    }

    public void setDeActivatedBy(Object deActivatedBy) {
        this.deActivatedBy = deActivatedBy;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Boolean getIsShown() {
        return isShown;
    }

    public void setIsShown(Boolean isShown) {
        this.isShown = isShown;
    }

    public Boolean getIsRemark() {
        return isRemark;
    }

    public void setIsRemark(Boolean isRemark) {
        this.isRemark = isRemark;
    }

}
