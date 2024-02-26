package com.android.urgetruck.UI.Models.exitclearancenew;

public class ExitInvoiceUpdateListResponse {
    private  String errorMessage;
    private  String exception;
    private  Boolean nextPage;
    private  String responseMessage;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    private  int statusCode;

    public ExitInvoiceUpdateListResponse(String errorMessage, String exception, boolean nextPage, String responseMessage, int statusCode) {
        this.errorMessage = errorMessage;
        this.exception = exception;
        this.nextPage = nextPage;
        this.responseMessage = responseMessage;
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getException() {
        return exception;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean getNextPage() {
        return nextPage;
    }
}
