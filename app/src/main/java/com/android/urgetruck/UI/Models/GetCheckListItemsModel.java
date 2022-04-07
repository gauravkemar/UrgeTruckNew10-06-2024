package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCheckListItemsModel {

    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusMessage")
    @Expose
    private Object statusMessage;
    @SerializedName("existCheckList")
    @Expose
    private List<ExistCheck> existCheckList = null;

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

    public List<ExistCheck> getExistCheckList() {
        return existCheckList;
    }

    public void setExistCheckList(List<ExistCheck> existCheckList) {
        this.existCheckList = existCheckList;
    }
}
