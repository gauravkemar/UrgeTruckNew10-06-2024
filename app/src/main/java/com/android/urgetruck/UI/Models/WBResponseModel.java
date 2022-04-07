package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WBResponseModel {
    public List<WBListResultModel> getWbListResultModels() {
        return wbListResultModels;
    }

    public void setWbListResultModels(List<WBListResultModel> wbListResultModels) {
        this.wbListResultModels = wbListResultModels;
    }

    @SerializedName("weighBridgeList")
    @Expose
    private List<WBListResultModel> wbListResultModels = null;
}
