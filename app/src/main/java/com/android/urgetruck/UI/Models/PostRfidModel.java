package com.android.urgetruck.UI.Models;

public class PostRfidModel {


    String RequestId;
    String RFIDTagNo;
    String DevicelocationId;
    String VRN;

    public PostRfidModel(String requestId, String RFIDTagNo, String devicelocationId, String VRN, String reason) {
        RequestId = requestId;
        this.RFIDTagNo = RFIDTagNo;
        DevicelocationId = devicelocationId;
        this.VRN = VRN;
        Reason = reason;
    }

    String Reason;

    public PostRfidResultModel getPostRfidResultModel() {
        return postRfidResultModel;
    }

    public void setPostRfidResultModel(PostRfidResultModel postRfidResultModel) {
        this.postRfidResultModel = postRfidResultModel;
    }

    PostRfidResultModel postRfidResultModel;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getRFIDTagNo() {
        return RFIDTagNo;
    }

    public void setRFIDTagNo(String RFIDTagNo) {
        this.RFIDTagNo = RFIDTagNo;
    }

    public String getDevicelocationId() {
        return DevicelocationId;
    }

    public void setDevicelocationId(String devicelocationId) {
        DevicelocationId = devicelocationId;
    }

    public String getVRN() {
        return VRN;
    }

    public void setVRN(String VRN) {
        this.VRN = VRN;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }


}
