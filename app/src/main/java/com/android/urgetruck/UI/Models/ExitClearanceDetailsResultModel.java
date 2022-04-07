package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExitClearanceDetailsResultModel {
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusMassage")
    @Expose
    private Object statusMassage;
    @SerializedName("exitClearanceDetails")
    @Expose
    private ExitClearanceDetails exitClearanceDetails;

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

    public Object getStatusMassage() {
        return statusMassage;
    }

    public void setStatusMassage(Object statusMassage) {
        this.statusMassage = statusMassage;
    }

    public ExitClearanceDetails getExitClearanceDetails() {
        return exitClearanceDetails;
    }

    public void setExitClearanceDetails(ExitClearanceDetails exitClearanceDetails) {
        this.exitClearanceDetails = exitClearanceDetails;
    }

}
