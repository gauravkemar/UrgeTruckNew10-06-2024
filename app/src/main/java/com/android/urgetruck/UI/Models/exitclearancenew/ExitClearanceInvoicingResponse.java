package com.android.urgetruck.UI.Models.exitclearancenew;

import java.util.List;


public class ExitClearanceInvoicingResponse {
    private final String driverCode;
    private final String driverName;
    private final String errorMessage;
    private final List<InvoiceDetail> invoiceDetail;
    private final int jobMilestoneId;
    private final String milestoneCode;
    private final String milestoneStatus;
    private final String milestoneTransactionCode;
    private final String status;
    private final int vehicelTransactionId;
    private final String vrn;

    public ExitClearanceInvoicingResponse(String driverCode, String driverName, String errorMessage, List<InvoiceDetail> invoiceDetail,
                           int jobMilestoneId, String milestoneCode, String milestoneStatus, String milestoneTransactionCode,
                           String status, int vehicelTransactionId, String vrn) {
        this.driverCode = driverCode;
        this.driverName = driverName;
        this.errorMessage = errorMessage;
        this.invoiceDetail = invoiceDetail;
        this.jobMilestoneId = jobMilestoneId;
        this.milestoneCode = milestoneCode;
        this.milestoneStatus = milestoneStatus;
        this.milestoneTransactionCode = milestoneTransactionCode;
        this.status = status;
        this.vehicelTransactionId = vehicelTransactionId;
        this.vrn = vrn;
    }

    // Getters (and setters if needed)
    public String getDriverCode() {
        return driverCode;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<InvoiceDetail> getInvoiceDetail() {
        return invoiceDetail;
    }

    public int getJobMilestoneId() {
        return jobMilestoneId;
    }

    public String getMilestoneCode() {
        return milestoneCode;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    public String getMilestoneTransactionCode() {
        return milestoneTransactionCode;
    }

    public String getStatus() {
        return status;
    }

    public int getVehicelTransactionId() {
        return vehicelTransactionId;
    }

    public String getVrn() {
        return vrn;
    }


}



/*
public class ExitClearanceInvoicingResponse {

        private String driverCode;
        private String driverName;
        private Object errorMessage;
        private List<InvoiceDetail> invoiceDetail;
        private int jobMilestoneId;
        private String milestoneCode;
        private String milestoneStatus;
        private String status;
        private int vehicelTransactionId;
        private String vrn;
        private String milestoneTransactionCode;

    public String getMilestoneTransactionCode() {
        return milestoneTransactionCode;
    }

    public void setMilestoneTransactionCode(String milestoneTransactionCode) {
        this.milestoneTransactionCode = milestoneTransactionCode;
    }

    public ExitClearanceInvoicingResponse(String driverCode, String driverName, Object errorMessage, List<InvoiceDetail> invoiceDetail, int jobMilestoneId, String milestoneCode, String milestoneStatus, String status, int vehicelTransactionId, String vrn,String milestoneTransactionCode) {
            this.driverCode = driverCode;
            this.driverName = driverName;
            this.errorMessage = errorMessage;
            this.invoiceDetail = invoiceDetail;
            this.jobMilestoneId = jobMilestoneId;
            this.milestoneCode = milestoneCode;
            this.milestoneStatus = milestoneStatus;
            this.status = status;
            this.vehicelTransactionId = vehicelTransactionId;
            this.vrn = vrn;
            this.milestoneTransactionCode = milestoneTransactionCode;
        }

        public String getDriverCode() {
            return driverCode;
        }

        public void setDriverCode(String driverCode) {
            this.driverCode = driverCode;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public Object getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(Object errorMessage) {
            this.errorMessage = errorMessage;
        }

        public List<InvoiceDetail> getInvoiceDetail() {
            return invoiceDetail;
        }

        public void setInvoiceDetail(List<InvoiceDetail> invoiceDetail) {
            this.invoiceDetail = invoiceDetail;
        }

        public int getJobMilestoneId() {
            return jobMilestoneId;
        }

        public void setJobMilestoneId(int jobMilestoneId) {
            this.jobMilestoneId = jobMilestoneId;
        }

        public String getMilestoneCode() {
            return milestoneCode;
        }

        public void setMilestoneCode(String milestoneCode) {
            this.milestoneCode = milestoneCode;
        }

        public String getMilestoneStatus() {
            return milestoneStatus;
        }

        public void setMilestoneStatus(String milestoneStatus) {
            this.milestoneStatus = milestoneStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getVehicelTransactionId() {
            return vehicelTransactionId;
        }

        public void setVehicelTransactionId(int vehicelTransactionId) {
            this.vehicelTransactionId = vehicelTransactionId;
        }

        public String getVrn() {
            return vrn;
        }

        public void setVrn(String vrn) {
            this.vrn = vrn;
        }

}
*/
