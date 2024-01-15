package com.android.urgetruck.UI.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.android.urgetruck.UI.Models.invoicecheckingstar.GetLoadingDetailOnVehicleDetailResponse;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneRequest;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneResponse;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UTRepository {

    private MutableLiveData<GetLoadingDetailOnVehicleDetailResponse> mutableLiveData=new MutableLiveData<>();
    private Application application;
    private String baseUrl;

    public UTRepository(Application application) {
        this.application = application;
    }
    public MutableLiveData<GetLoadingDetailOnVehicleDetailResponse> getMutableLiveData(String baseUrl,int requestID, String vrn, String rfid) {
        this.baseUrl=baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<GetLoadingDetailOnVehicleDetailResponse> call=apiService.GetLoadingDetailOnVehicleDetail(requestID,vrn,rfid);
        call.enqueue(new Callback<GetLoadingDetailOnVehicleDetailResponse>() {
            @Override
            public void onResponse(Call<GetLoadingDetailOnVehicleDetailResponse> call, Response<GetLoadingDetailOnVehicleDetailResponse> response) {
                //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                GetLoadingDetailOnVehicleDetailResponse getLoadingDetailOnVehicleDetailResponse=response.body();
                if(getLoadingDetailOnVehicleDetailResponse!=null )
                {

                    mutableLiveData.setValue(getLoadingDetailOnVehicleDetailResponse);
                }
            }
            @Override
            public void onFailure(Call<GetLoadingDetailOnVehicleDetailResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    private MutableLiveData<UpdateLoadingCompleteMilestoneResponse> updateLoadingCompleteMutable=new MutableLiveData<>();

    public MutableLiveData<UpdateLoadingCompleteMilestoneResponse> getUpdateLoadingLiveData(String baseUrl,UpdateLoadingCompleteMilestoneRequest updateLoadingCompleteMilestoneRequest) {
        this.baseUrl=baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<UpdateLoadingCompleteMilestoneResponse> call=apiService.updateLoadingCompleteMilestone(updateLoadingCompleteMilestoneRequest);
        call.enqueue(new Callback<UpdateLoadingCompleteMilestoneResponse>() {
            @Override
            public void onResponse(Call<UpdateLoadingCompleteMilestoneResponse> call, Response<UpdateLoadingCompleteMilestoneResponse> response) {
                //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                UpdateLoadingCompleteMilestoneResponse getLoadingDetailOnVehicleDetailResponse=response.body();
                if(getLoadingDetailOnVehicleDetailResponse!=null )
                {

                    updateLoadingCompleteMutable.setValue(getLoadingDetailOnVehicleDetailResponse);
                }
            }
            @Override
            public void onFailure(Call<UpdateLoadingCompleteMilestoneResponse> call, Throwable t) {

            }
        });
        return updateLoadingCompleteMutable;
    }


}