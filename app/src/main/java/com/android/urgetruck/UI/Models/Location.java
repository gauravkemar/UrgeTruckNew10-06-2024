package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("deviceLocationId")
    @Expose
    private Integer deviceLocationId;
    @SerializedName("deviceName")
    @Expose
    private String deviceName;

    public Integer getDeviceLocationId() {
        return deviceLocationId;
    }

    public void setDeviceLocationId(Integer deviceLocationId) {
        this.deviceLocationId = deviceLocationId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
