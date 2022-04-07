package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WBListResultModel {

    @SerializedName("weighBridgeId")
    @Expose
    private int wbId;

    public WBListResultModel() {

    }

    public int getWbId() {
        return wbId;
    }

    public void setWbId(int wbId) {
        this.wbId = wbId;
    }

    public String getWbName() {
        return wbName;
    }

    public void setWbName(String wbName) {
        this.wbName = wbName;
    }

    public WBListResultModel(int wbId, String wbName) {
        this.wbId = wbId;
        this.wbName = wbName;
    }

    @SerializedName("weighBridgeName")
    @Expose
    private String wbName;
}
