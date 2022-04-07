package com.android.urgetruck.UI.Models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecurityCheckModel {
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("RFIDTagNo")
    @Expose
    private String rFIDTagNo;
    @SerializedName("jobMilestoneId")
    @Expose
    private String jobMilestoneId;
    @SerializedName("vehicleTransactionId")
    @Expose
    private String vehicleTransactionId;
    @SerializedName("VRN")
    @Expose
    private String vrn;

    @SerializedName("weighBridgeId")
    @Expose
    private int wbId;

    public int getWbId() {
        return wbId;
    }

    public void setWbId(int wbId) {
        this.wbId = wbId;
    }

    public SecurityCheckModel(String requestId, String rFIDTagNo, String jobMilestoneId, String vehicleTransactionId, String vrn, String reason, String accept, int wbId) {
        this.requestId = requestId;
        this.rFIDTagNo = rFIDTagNo;
        this.jobMilestoneId = jobMilestoneId;
        this.vehicleTransactionId = vehicleTransactionId;
        this.vrn = vrn;
        this.reason = reason;
        this.accept = accept;
        this.wbId = wbId;
        Log.e("urlbody",requestId+"||"+rFIDTagNo+"||"+jobMilestoneId+"||"+vehicleTransactionId+"||"+vrn+"||"+reason+"||"+accept+"||"+wbId);
    }

    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Accept")
    @Expose
    private String accept;

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

    public String getJobMilestoneId() {
        return jobMilestoneId;
    }

    public void setJobMilestoneId(String jobMilestoneId) {
        this.jobMilestoneId = jobMilestoneId;
    }

    public String getVehicleTransactionId() {
        return vehicleTransactionId;
    }

    public void setVehicleTransactionId(String vehicleTransactionId) {
        this.vehicleTransactionId = vehicleTransactionId;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

}
