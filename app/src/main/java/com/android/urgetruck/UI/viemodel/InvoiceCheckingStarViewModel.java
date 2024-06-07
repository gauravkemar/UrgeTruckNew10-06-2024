package com.android.urgetruck.UI.viemodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.urgetruck.UI.Models.exitclearancenew.ExitClearanceInvoicingResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.ExitClearanceProductVerificationResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.ExitInvoiceUpdateListResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.GeneralResponse;
import com.android.urgetruck.UI.Models.invoicecheckingstar.GetLoadingDetailOnVehicleDetailResponse;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneRequest;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneResponse;
import com.android.urgetruck.UI.repository.UTRepository;
import com.android.urgetruck.UI.view.ExitClearanceNewActivity;

public class InvoiceCheckingStarViewModel extends AndroidViewModel {
    private UTRepository repository;
    public InvoiceCheckingStarViewModel(@NonNull Application application) {
        super(application);
        repository=new UTRepository(application);
    }
    public LiveData<GetLoadingDetailOnVehicleDetailResponse> getLoadingDetailOnVehicleResponse(String baseUrl, int RequestID, String vrn, String Rfid  ){
        return repository.getMutableLiveData(baseUrl,RequestID,vrn,Rfid);
    }
    public LiveData<UpdateLoadingCompleteMilestoneResponse> updateLoadingCompleteMilestone(String baseUrl,UpdateLoadingCompleteMilestoneRequest updateLoadingCompleteMilestoneRequest ){
        return repository.getUpdateLoadingLiveData(baseUrl,updateLoadingCompleteMilestoneRequest);
    }
    public LiveData<ExitClearanceInvoicingResponse> getExitClearanceInvoiceMutableLiveData(ExitClearanceNewActivity exitClearanceNewActivity, String baseUrl, int RequestID, String vrn, String Rfid  ){
        return repository.getExitClearanceInvoiceMutableLiveData(exitClearanceNewActivity,baseUrl,RequestID,vrn,Rfid);
    }
    public LiveData<ExitInvoiceUpdateListResponse> updateExitClearanceInvoiceMutableLiveData(ExitClearanceNewActivity exitClearanceNewActivity, String baseUrl, ExitClearanceInvoicingResponse updateExitClearanceInvoicingResponse ){
        return repository.updateExitClearanceInvoiceMutableLiveData (exitClearanceNewActivity,baseUrl,updateExitClearanceInvoicingResponse);
    }

//product vetification
    public LiveData<ExitClearanceProductVerificationResponse> getExitClearanceProductVerification(ExitClearanceNewActivity exitClearanceNewActivity, String baseUrl, int RequestID, String vrn, String Rfid  ){
        return repository.getExitClearanceProductVerification(exitClearanceNewActivity,baseUrl,RequestID,vrn,Rfid);
    }
    public LiveData<ExitInvoiceUpdateListResponse> updateProductDetailOnVehicleDetailMutableLiveData(ExitClearanceNewActivity exitClearanceNewActivity, String baseUrl, ExitClearanceProductVerificationResponse updateExitClearanceInvoicingResponse ){
        return repository.updateProductDetailOnVehicleDetailMutableLiveData (exitClearanceNewActivity,baseUrl,updateExitClearanceInvoicingResponse);
    }



////
    public LiveData<GeneralResponse> updateExitClearancePhysicalMutableLiveData(String baseUrl, ExitClearanceInvoicingResponse updateExitClearanceInvoicingResponse ){
        return repository.updateExitClearancePhysicalMutableLiveData (baseUrl,updateExitClearanceInvoicingResponse);
    }
}
