package com.android.urgetruck.UI.viemodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.urgetruck.UI.Models.invoicecheckingstar.GetLoadingDetailOnVehicleDetailResponse;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneRequest;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneResponse;
import com.android.urgetruck.UI.repository.UTRepository;

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
}
