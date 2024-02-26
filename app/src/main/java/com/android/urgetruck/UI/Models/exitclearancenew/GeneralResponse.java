package com.android.urgetruck.UI.Models.exitclearancenew;

public class GeneralResponse {

        public String errorMessage;
        public String  responseMessage;
        public String status;
        public String exception;

    public GeneralResponse(String errorMessage, String responseMessage, String status, String exception) {
        this.errorMessage = errorMessage;
        this.responseMessage = responseMessage;
        this.status = status;
        this.exception = exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
