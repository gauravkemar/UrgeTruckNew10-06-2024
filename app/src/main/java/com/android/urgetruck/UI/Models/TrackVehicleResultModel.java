package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackVehicleResultModel {
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusMessage")
    @Expose
    private String  statusMessage;
    @SerializedName("vehicleTransactionDetails")
    @Expose
    private VehicleTransactionDetails vehicleTransactionDetails;

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

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public VehicleTransactionDetails getVehicleTransactionDetails() {
        return vehicleTransactionDetails;
    }

    public void setVehicleTransactionDetails(VehicleTransactionDetails vehicleTransactionDetails) {
        this.vehicleTransactionDetails = vehicleTransactionDetails;
    }

}
