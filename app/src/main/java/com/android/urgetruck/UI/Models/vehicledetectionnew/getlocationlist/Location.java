package com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationlist;

import java.util.List;

public class Location {
        private String createdBy;
        private String createdDate;
        private List<Object> currentQueue; // Adjust this based on the actual type
        private String detectableBy;
        private String displayName;
        private boolean isActive;
        private String locationCode;
        private int locationId;
        private String locationName;
        private Object locationType; // Adjust this based on the actual type
        private int maxQueueSize;
        private int minQueueSize;
        private Object modifiedBy; // Adjust this based on the actual type
        private Object modifiedDate; // Adjust this based on the actual type
        private Object parentLocationCode; // Adjust this based on the actual type
        private int sequence;
        private List<Object> weighBridgeMaster; // Adjust this based on the actual type

        // Constructor
        public Location(String createdBy, String createdDate, List<Object> currentQueue, String detectableBy,
                        String displayName, boolean isActive, String locationCode, int locationId,
                        String locationName, Object locationType, int maxQueueSize, int minQueueSize,
                        Object modifiedBy, Object modifiedDate, Object parentLocationCode, int sequence,
                        List<Object> weighBridgeMaster) {
            this.createdBy = createdBy;
            this.createdDate = createdDate;
            this.currentQueue = currentQueue;
            this.detectableBy = detectableBy;
            this.displayName = displayName;
            this.isActive = isActive;
            this.locationCode = locationCode;
            this.locationId = locationId;
            this.locationName = locationName;
            this.locationType = locationType;
            this.maxQueueSize = maxQueueSize;
            this.minQueueSize = minQueueSize;
            this.modifiedBy = modifiedBy;
            this.modifiedDate = modifiedDate;
            this.parentLocationCode = parentLocationCode;
            this.sequence = sequence;
            this.weighBridgeMaster = weighBridgeMaster;
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

    public Object getLocationType() {
        return locationType;
    }

    public void setLocationType(Object locationType) {
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

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Object getParentLocationCode() {
        return parentLocationCode;
    }

    public void setParentLocationCode(Object parentLocationCode) {
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
}
