package com.android.urgetruck.UI.Models.exitclearancenew;

public class InvoiceDetail {

        private String invoiceNumber;
        private boolean isActive;
        private boolean isVerified;
        private String status;
        private int vehicelTransactionId;

        public InvoiceDetail(String invoiceNumber, boolean isActive, boolean isVerified, String status, int vehicelTransactionId) {
            this.invoiceNumber = invoiceNumber;
            this.isActive = isActive;
            this.isVerified = isVerified;
            this.status = status;
            this.vehicelTransactionId = vehicelTransactionId;
        }

        public String getInvoiceNumber() {
            return invoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public boolean isVerified() {
            return isVerified;
        }

        public void setVerified(boolean verified) {
            isVerified = verified;
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

    public void updateFields(boolean newIsActive, boolean newIsVerified, String newStatus) {
        this.isActive = newIsActive;
        this.isVerified = newIsVerified;
        this.status = newStatus;
    }


 /*   @Override
    public String toString() {
        return "InvoiceDetail{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", status='" + status + '\'' +
                ", vehicelTransactionId='" + vehicelTransactionId + '\'' +
                '}';
    }
*/
}
