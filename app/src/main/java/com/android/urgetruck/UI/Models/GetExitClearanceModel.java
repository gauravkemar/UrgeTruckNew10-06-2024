package com.android.urgetruck.UI.Models;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetExitClearanceModel implements Serializable {
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusMessage")
    @Expose
    private Object statusMessage;
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

    public Object getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(Object statusMessage) {
        this.statusMessage = statusMessage;
    }

    public ExitClearanceDetails getExitClearanceDetails() {
        return exitClearanceDetails;
    }

    public void setExitClearanceDetails(ExitClearanceDetails exitClearanceDetails) {
        this.exitClearanceDetails = exitClearanceDetails;
    }
}
