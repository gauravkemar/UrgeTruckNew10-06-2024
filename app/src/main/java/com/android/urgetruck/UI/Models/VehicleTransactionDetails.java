
package com.android.urgetruck.UI.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleTransactionDetails {

    @SerializedName("vehicleTransactionId")
    @Expose
    private Integer vehicleTransactionId;
    @SerializedName("vehicleTransactionCode")
    @Expose
    private String vehicleTransactionCode;
    @SerializedName("vrn")
    @Expose
    private String vrn;
    @SerializedName("driverId")
    @Expose
    private Integer driverId;
    @SerializedName("rfidTagNumber")
    @Expose
    private String rfidTagNumber;
    @SerializedName("tranType")
    @Expose
    private Integer tranType;
    @SerializedName("shipmentNo")
    @Expose
    private String shipmentNo;
    @SerializedName("gateEntryNo")
    @Expose
    private String gateEntryNo;
    @SerializedName("transactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("transactionStartTime")
    @Expose
    private Object transactionStartTime;
    @SerializedName("transactionEndTime")
    @Expose
    private Object transactionEndTime;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("tranStatus")
    @Expose
    private String tranStatus;
    @SerializedName("remarks")
    @Expose
    private Object remarks;
    @SerializedName("driverName")
    @Expose
    private String driverName;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("jobMilestones")
    @Expose
    private List<JobMilestone> jobMilestones = null;

    public Integer getVehicleTransactionId() {
        return vehicleTransactionId;
    }

    public void setVehicleTransactionId(Integer vehicleTransactionId) {
        this.vehicleTransactionId = vehicleTransactionId;
    }

    public String getVehicleTransactionCode() {
        return vehicleTransactionCode;
    }

    public void setVehicleTransactionCode(String vehicleTransactionCode) {
        this.vehicleTransactionCode = vehicleTransactionCode;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getRfidTagNumber() {
        return rfidTagNumber;
    }

    public void setRfidTagNumber(String rfidTagNumber) {
        this.rfidTagNumber = rfidTagNumber;
    }

    public Integer getTranType() {
        return tranType;
    }

    public void setTranType(Integer tranType) {
        this.tranType = tranType;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getGateEntryNo() {
        return gateEntryNo;
    }

    public void setGateEntryNo(String gateEntryNo) {
        this.gateEntryNo = gateEntryNo;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Object getTransactionStartTime() {
        return transactionStartTime;
    }

    public void setTransactionStartTime(Object transactionStartTime) {
        this.transactionStartTime = transactionStartTime;
    }

    public Object getTransactionEndTime() {
        return transactionEndTime;
    }

    public void setTransactionEndTime(Object transactionEndTime) {
        this.transactionEndTime = transactionEndTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<JobMilestone> getJobMilestones() {
        return jobMilestones;
    }

    public void setJobMilestones(List<JobMilestone> jobMilestones) {
        this.jobMilestones = jobMilestones;
    }

}
