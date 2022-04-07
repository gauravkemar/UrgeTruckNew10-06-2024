package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationModel {

    @SerializedName("requestId")
    @Expose
    private Object requestId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusMassage")
    @Expose
    private Object statusMassage;
    @SerializedName("locations")
    @Expose
    private List<Location> locations = null;

    public Object getRequestId() {
        return requestId;
    }

    public void setRequestId(Object requestId) {
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
