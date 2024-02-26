package com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationmasterdatabylocationId;

import java.util.List;

public class DeviceLocationMapping {
    private Object accessURL;
    private int antenna;
    private String createdBy;
    private String createdDate;
    private String deviceIP;
    private int deviceLocationMappingId;
    private String deviceName;
    private List<Object> deviceStatus;
    private String deviceType;
    private String direction;
    private List<Object> gpoManager;
    private boolean isActive;
    private String lane;
    private Object ledNoOfLines;
    private int locationId;
    private String modifiedBy;
    private String modifiedDate;
    private Object portNo;
    private String remark;
    private Object snapCaptureURL;

    public DeviceLocationMapping(Object accessURL, int antenna, String createdBy, String createdDate,
                                 String deviceIP, int deviceLocationMappingId, String deviceName,
                                 List<Object> deviceStatus, String deviceType, String direction,
                                 List<Object> gpoManager, boolean isActive, String lane,
                                 Object ledNoOfLines, int locationId, String modifiedBy,
                                 String modifiedDate, Object portNo, String remark,
                                 Object snapCaptureURL) {
        this.accessURL = accessURL;
        this.antenna = antenna;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.deviceIP = deviceIP;
        this.deviceLocationMappingId = deviceLocationMappingId;
        this.deviceName = deviceName;
        this.deviceStatus = deviceStatus;
        this.deviceType = deviceType;
        this.direction = direction;
        this.gpoManager = gpoManager;
        this.isActive = isActive;
        this.lane = lane;
        this.ledNoOfLines = ledNoOfLines;
        this.locationId = locationId;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.portNo = portNo;
        this.remark = remark;
        this.snapCaptureURL = snapCaptureURL;
    }

    public Object getAccessURL() {
        return accessURL;
    }

    public void setAccessURL(Object accessURL) {
        this.accessURL = accessURL;
    }

    public int getAntenna() {
        return antenna;
    }

    public void setAntenna(int antenna) {
        this.antenna = antenna;
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

    public String getDeviceIP() {
        return deviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public int getDeviceLocationMappingId() {
        return deviceLocationMappingId;
    }

    public void setDeviceLocationMappingId(int deviceLocationMappingId) {
        this.deviceLocationMappingId = deviceLocationMappingId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<Object> getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(List<Object> deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Object> getGpoManager() {
        return gpoManager;
    }

    public void setGpoManager(List<Object> gpoManager) {
        this.gpoManager = gpoManager;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public Object getLedNoOfLines() {
        return ledNoOfLines;
    }

    public void setLedNoOfLines(Object ledNoOfLines) {
        this.ledNoOfLines = ledNoOfLines;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
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

    public Object getPortNo() {
        return portNo;
    }

    public void setPortNo(Object portNo) {
        this.portNo = portNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getSnapCaptureURL() {
        return snapCaptureURL;
    }

    public void setSnapCaptureURL(Object snapCaptureURL) {
        this.snapCaptureURL = snapCaptureURL;
    }
}