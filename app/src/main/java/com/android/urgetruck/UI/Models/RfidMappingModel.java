package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RfidMappingModel {

    @SerializedName("RequestId")
    @Expose
    private String requestId;
    @SerializedName("VRN")
    @Expose
    private String vrn;
    @SerializedName("RFIDTagNo")
    @Expose
    private String rFIDTagNo;

    public RfidMappingModel(String requestId, String vrn, String rFIDTagNo, String forceMap) {
        this.requestId = requestId;
        this.vrn = vrn;
        this.rFIDTagNo = rFIDTagNo;
        this.forceMap = forceMap;
    }

    @SerializedName("ForceMap")
    @Expose
    private String forceMap;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }

    public String getRFIDTagNo() {
        return rFIDTagNo;
    }

    public void setRFIDTagNo(String rFIDTagNo) {
        this.rFIDTagNo = rFIDTagNo;
    }

    public String getForceMap() {
        return forceMap;
    }

    public void setForceMap(String forceMap) {
        this.forceMap = forceMap;
    }

}
