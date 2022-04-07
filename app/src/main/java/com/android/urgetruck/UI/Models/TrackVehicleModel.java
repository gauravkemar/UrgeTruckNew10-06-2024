package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackVehicleModel {
    @SerializedName("RequestId")
    @Expose
    private String requestId;

    public TrackVehicleModel(String requestId, String rFIDTagNo, String vrn) {
        this.requestId = requestId;
        this.rFIDTagNo = rFIDTagNo;
        this.vrn = vrn;
    }

    @SerializedName("RFIDTagNo")
    @Expose
    private String rFIDTagNo;
    @SerializedName("VRN")
    @Expose
    private String vrn;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRFIDTagNo() {
        return rFIDTagNo;
    }

    public void setRFIDTagNo(String rFIDTagNo) {
        this.rFIDTagNo = rFIDTagNo;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }
}
