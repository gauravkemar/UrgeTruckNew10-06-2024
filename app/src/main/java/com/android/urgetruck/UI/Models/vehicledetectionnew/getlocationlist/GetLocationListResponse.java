package com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLocationListResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;

    @SerializedName("locations")
    @Expose
    private List<Location> locations;

    public GetLocationListResponse(String status, String statusMessage, List<Location> locations) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.locations = locations;
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

}
