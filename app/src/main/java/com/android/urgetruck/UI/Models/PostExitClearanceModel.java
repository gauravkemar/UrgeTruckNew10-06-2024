package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostExitClearanceModel {
    @SerializedName("RequestId")
    @Expose
    private String requestId;

    public PostExitClearanceModel(String requestId, String vrn, String vehicleTransactionId, String jobMilestoneId, List<ExitClearanceParameters> parameters) {
        this.requestId = requestId;
        this.vrn = vrn;
        this.vehicleTransactionId = vehicleTransactionId;
        this.jobMilestoneId = jobMilestoneId;
        this.parameters = parameters;
    }

    @SerializedName("VRN")
    @Expose
    private String vrn;
    @SerializedName("VehicleTransactionId")
    @Expose
    private String vehicleTransactionId;
    @SerializedName("JobMilestoneId")
    @Expose
    private String jobMilestoneId;
    @SerializedName("Parameters")
    @Expose
    private List<ExitClearanceParameters> parameters = null;

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

    public String getVehicleTransactionId() {
        return vehicleTransactionId;
    }

    public void setVehicleTransactionId(String vehicleTransactionId) {
        this.vehicleTransactionId = vehicleTransactionId;
    }

    public String getJobMilestoneId() {
        return jobMilestoneId;
    }

    public void setJobMilestoneId(String jobMilestoneId) {
        this.jobMilestoneId = jobMilestoneId;
    }

    public List<ExitClearanceParameters> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExitClearanceParameters> parameters) {
        this.parameters = parameters;
    }
}
