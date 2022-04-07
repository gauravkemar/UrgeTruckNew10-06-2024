package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeightDetailsResultModel {
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusMassage")
    @Expose
    private String statusMassage;
    @SerializedName("weighmentDetails")
    @Expose
    private WeighmentDetails weighmentDetails;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMassage() {
        return statusMassage;
    }

    public void setStatusMassage(String statusMassage) {
        this.statusMassage = statusMassage;
    }

    public WeighmentDetails getWeighmentDetails() {
        return weighmentDetails;
    }

    public void setWeighmentDetails(WeighmentDetails weighmentDetails) {
        this.weighmentDetails = weighmentDetails;
    }
}
