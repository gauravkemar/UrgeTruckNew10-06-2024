package com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationmasterdatabylocationId;

import java.util.List;

public class MasterLocation {
    private String createdBy;
    private String createdDate;
    private List<Object> currentQueue;
    private String detectableBy;
    private List<DeviceLocationMapping> deviceLocationMapping;
    private String displayName;
    private boolean isActive;
    private List<Object> ledNotification;
    private List<Object> locationClosingTime;
    private String locationCode;
    private int locationId;
    private String locationName;
    private List<Object> locationStatusHistory;
    private String locationType;
    private int maxQueueSize;
    private int minQueueSize;
    private String modifiedBy;
    private String modifiedDate;
    private List<Object> packerMaster;
    private String parentLocationCode;
    private int sequence;
    private List<Object> weighBridgeMaster;
    private List<Object> weighbridgeAllocationPerferences;

    public MasterLocation(String createdBy, String createdDate, List<Object> currentQueue,
                          String detectableBy, List<DeviceLocationMapping> deviceLocationMapping,
                          String displayName, boolean isActive, List<Object> ledNotification,
                          List<Object> locationClosingTime, String locationCode, int locationId,
                          String locationName, List<Object> locationStatusHistory, String locationType,
                          int maxQueueSize, int minQueueSize, String modifiedBy, String modifiedDate,
                          List<Object> packerMaster, String parentLocationCode, int sequence,
                          List<Object> weighBridgeMaster, List<Object> weighbridgeAllocationPerferences) {
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.currentQueue = currentQueue;
        this.detectableBy = detectableBy;
        this.deviceLocationMapping = deviceLocationMapping;
        this.displayName = displayName;
        this.isActive = isActive;
        this.ledNotification = ledNotification;
        this.locationClosingTime = locationClosingTime;
        this.locationCode = locationCode;
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationStatusHistory = locationStatusHistory;
        this.locationType = locationType;
        this.maxQueueSize = maxQueueSize;
        this.minQueueSize = minQueueSize;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.packerMaster = packerMaster;
        this.parentLocationCode = parentLocationCode;
        this.sequence = sequence;
        this.weighBridgeMaster = weighBridgeMaster;
        this.weighbridgeAllocationPerferences = weighbridgeAllocationPerferences;
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

    public List<Object> getCurrentQueue() {
        return currentQueue;
    }

    public void setCurrentQueue(List<Object> currentQueue) {
        this.currentQueue = currentQueue;
    }

    public String getDetectableBy() {
        return detectableBy;
    }

    public void setDetectableBy(String detectableBy) {
        this.detectableBy = detectableBy;
    }

    public List<DeviceLocationMapping> getDeviceLocationMapping() {
        return deviceLocationMapping;
    }

    public void setDeviceLocationMapping(List<DeviceLocationMapping> deviceLocationMapping) {
        this.deviceLocationMapping = deviceLocationMapping;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Object> getLedNotification() {
        return ledNotification;
    }

    public void setLedNotification(List<Object> ledNotification) {
        this.ledNotification = ledNotification;
    }

    public List<Object> getLocationClosingTime() {
        return locationClosingTime;
    }

    public void setLocationClosingTime(List<Object> locationClosingTime) {
        this.locationClosingTime = locationClosingTime;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
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

    public List<Object> getLocationStatusHistory() {
        return locationStatusHistory;
    }

    public void setLocationStatusHistory(List<Object> locationStatusHistory) {
        this.locationStatusHistory = locationStatusHistory;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
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

    public List<Object> getPackerMaster() {
        return packerMaster;
    }

    public void setPackerMaster(List<Object> packerMaster) {
        this.packerMaster = packerMaster;
    }

    public String getParentLocationCode() {
        return parentLocationCode;
    }

    public void setParentLocationCode(String parentLocationCode) {
        this.parentLocationCode = parentLocationCode;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public List<Object> getWeighBridgeMaster() {
        return weighBridgeMaster;
    }

    public void setWeighBridgeMaster(List<Object> weighBridgeMaster) {
        this.weighBridgeMaster = weighBridgeMaster;
    }

    public List<Object> getWeighbridgeAllocationPerferences() {
        return weighbridgeAllocationPerferences;
    }

    public void setWeighbridgeAllocationPerferences(List<Object> weighbridgeAllocationPerferences) {
        this.weighbridgeAllocationPerferences = weighbridgeAllocationPerferences;
    }
}