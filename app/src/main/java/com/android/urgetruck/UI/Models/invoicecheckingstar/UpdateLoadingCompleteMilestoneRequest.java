package com.android.urgetruck.UI.Models.invoicecheckingstar;

import java.util.List;

public class UpdateLoadingCompleteMilestoneRequest {
    private String milestonecode;
    private String milestonetransactioncode;
    private List<ProductX> products;
    private String requestid;
    private String status;
    private String vrn;

    public UpdateLoadingCompleteMilestoneRequest(String milestonecode, String milestonetransactioncode, List<ProductX> products, String requestid, String status, String vrn) {
        this.milestonecode = milestonecode;
        this.milestonetransactioncode = milestonetransactioncode;
        this.products = products;
        this.requestid = requestid;
        this.status = status;
        this.vrn = vrn;
    }

    public String getMilestonecode() {
        return milestonecode;
    }

    public void setMilestonecode(String milestonecode) {
        this.milestonecode = milestonecode;
    }

    public String getMilestonetransactioncode() {
        return milestonetransactioncode;
    }

    public void setMilestonetransactioncode(String milestonetransactioncode) {
        this.milestonetransactioncode = milestonetransactioncode;
    }

    public List<ProductX> getProducts() {
        return products;
    }

    public void setProducts(List<ProductX> products) {
        this.products = products;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }
}
