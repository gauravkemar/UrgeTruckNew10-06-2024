package com.android.urgetruck.UI.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExistCheck {
    @SerializedName("checklistItemId")
    @Expose
    private Integer checklistItemId;
    @SerializedName("checklistItem")
    @Expose
    private String checklistItem;
    @SerializedName("isMandatory")
    @Expose
    private Boolean isMandatory;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Integer getChecklistItemId() {
        return checklistItemId;
    }

    public void setChecklistItemId(Integer checklistItemId) {
        this.checklistItemId = checklistItemId;
    }

    public String getChecklistItem() {
        return checklistItem;
    }

    public void setChecklistItem(String checklistItem) {
        this.checklistItem = checklistItem;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
