
package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeighBridgeTransaction {

    @SerializedName("weighBridgeTransactionId")
    @Expose
    private Integer weighBridgeTransactionId;
    @SerializedName("jobMilestoneId")
    @Expose
    private Integer jobMilestoneId;
    @SerializedName("weighBridgeId")
    @Expose
    private Integer weighBridgeId;
    @SerializedName("actualTareweight")
    @Expose
    private Object actualTareweight;
    @SerializedName("actualWeight")
    @Expose
    private Double actualWeight;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("transactionDateTime")
    @Expose
    private String transactionDateTime;
    @SerializedName("isImageCaptured")
    @Expose
    private Boolean isImageCaptured;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getWeighBridgeTransactionId() {
        return weighBridgeTransactionId;
    }

    public void setWeighBridgeTransactionId(Integer weighBridgeTransactionId) {
        this.weighBridgeTransactionId = weighBridgeTransactionId;
    }

    public Integer getJobMilestoneId() {
        return jobMilestoneId;
    }

    public void setJobMilestoneId(Integer jobMilestoneId) {
        this.jobMilestoneId = jobMilestoneId;
    }

    public Integer getWeighBridgeId() {
        return weighBridgeId;
    }

    public void setWeighBridgeId(Integer weighBridgeId) {
        this.weighBridgeId = weighBridgeId;
    }

    public Object getActualTareweight() {
        return actualTareweight;
    }

    public void setActualTareweight(Object actualTareweight) {
        this.actualTareweight = actualTareweight;
    }

    public Double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(Double actualWeight) {
        this.actualWeight = actualWeight;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Boolean getIsImageCaptured() {
        return isImageCaptured;
    }

    public void setIsImageCaptured(Boolean isImageCaptured) {
        this.isImageCaptured = isImageCaptured;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
