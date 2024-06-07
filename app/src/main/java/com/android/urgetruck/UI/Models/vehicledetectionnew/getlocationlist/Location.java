package com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Location {
    @SerializedName("locationId")
    @Expose
    private int locationId;

    @SerializedName("locationName")
    @Expose
    private String locationName;

    @SerializedName("locationCode")
    @Expose
    private String locationCode;

    @SerializedName("parentLocationCode")
    @Expose
    private String parentLocationCode;

    @SerializedName("locationType")
    @Expose
    private String locationType;

    @SerializedName("sequence")
    @Expose
    private int sequence;

    @SerializedName("detectableBy")
    @Expose
    private String detectableBy;

    @SerializedName("isActive")
    @Expose
    private boolean isActive;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("maxQueueSize")
    @Expose
    private int maxQueueSize;

    @SerializedName("minQueueSize")
    @Expose
    private int minQueueSize;

    @SerializedName("createdBy")
    @Expose
    private String createdBy;

    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    @SerializedName("modifiedBy")
    @Expose
    private String modifiedBy;

    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

    @SerializedName("weighBridgeMaster")
    @Expose
    private List<Object> weighBridgeMaster;

    @SerializedName("currentQueue")
    @Expose
    private List<Object> currentQueue;

    public Location(int locationId, String locationName, String locationCode, String parentLocationCode, String locationType, int sequence, String detectableBy, boolean isActive, String displayName, int maxQueueSize, int minQueueSize, String createdBy, String createdDate, String modifiedBy, String modifiedDate, List<Object> weighBridgeMaster, List<Object> currentQueue) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationCode = locationCode;
        this.parentLocationCode = parentLocationCode;
        this.locationType = locationType;
        this.sequence = sequence;
        this.detectableBy = detectableBy;
        this.isActive = isActive;
        this.displayName = displayName;
        this.maxQueueSize = maxQueueSize;
        this.minQueueSize = minQueueSize;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.weighBridgeMaster = weighBridgeMaster;
        this.currentQueue = currentQueue;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getParentLocationCode() {
        return parentLocationCode;
    }

    public void setParentLocationCode(String parentLocationCode) {
        this.parentLocationCode = parentLocationCode;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getDetectableBy() {
        return detectableBy;
    }

    public void setDetectableBy(String detectableBy) {
        this.detectableBy = detectableBy;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public int getMinQueueSize() {
        return minQueueSize;
    }

    public void setMinQueueSize(int minQueueSize) {
        this.minQueueSize = minQueueSize;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<Object> getWeighBridgeMaster() {
        return weighBridgeMaster;
    }

    public void setWeighBridgeMaster(List<Object> weighBridgeMaster) {
        this.weighBridgeMaster = weighBridgeMaster;
    }

    public List<Object> getCurrentQueue() {
        return currentQueue;
    }

    public void setCurrentQueue(List<Object> currentQueue) {
        this.currentQueue = currentQueue;
    }
}
