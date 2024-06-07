package com.android.urgetruck.UI.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.android.urgetruck.UI.Models.exitclearancenew.ExitClearanceInvoicingResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.ExitClearanceProductVerificationResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.ExitInvoiceUpdateListResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.GeneralResponse;
import com.android.urgetruck.UI.Models.invoicecheckingstar.GetLoadingDetailOnVehicleDetailResponse;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneRequest;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneResponse;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.Utils;
import com.android.urgetruck.UI.view.ExitClearanceNewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UTRepository {

    private MutableLiveData<GetLoadingDetailOnVehicleDetailResponse> mutableLiveData = new MutableLiveData<>();
    private Application application;
    private String baseUrl;

    public UTRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<GetLoadingDetailOnVehicleDetailResponse> getMutableLiveData(String baseUrl, int requestID, String vrn, String rfid) {
        this.baseUrl = baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<GetLoadingDetailOnVehicleDetailResponse> call = apiService.GetLoadingDetailOnVehicleDetail(requestID, vrn, rfid);
        call.enqueue(new Callback<GetLoadingDetailOnVehicleDetailResponse>() {
            @Override
            public void onResponse(Call<GetLoadingDetailOnVehicleDetailResponse> call, Response<GetLoadingDetailOnVehicleDetailResponse> response) {
                //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                GetLoadingDetailOnVehicleDetailResponse getLoadingDetailOnVehicleDetailResponse = response.body();
                if (getLoadingDetailOnVehicleDetailResponse != null) {
                    mutableLiveData.setValue(getLoadingDetailOnVehicleDetailResponse);
                }
            }
            @Override
            public void onFailure(Call<GetLoadingDetailOnVehicleDetailResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(application.getApplicationContext(), "Ketwork error, Please check Network!!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(application.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Utils.showCustomDialog(application.getApplicationContext(),t.toString());
                }
            }
        });
        return mutableLiveData;
    }

    private MutableLiveData<UpdateLoadingCompleteMilestoneResponse> updateLoadingCompleteMutable = new MutableLiveData<>();

    public MutableLiveData<UpdateLoadingCompleteMilestoneResponse> getUpdateLoadingLiveData(String baseUrl, UpdateLoadingCompleteMilestoneRequest updateLoadingCompleteMilestoneRequest) {
        this.baseUrl = baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<UpdateLoadingCompleteMilestoneResponse> call = apiService.updateLoadingCompleteMilestone(updateLoadingCompleteMilestoneRequest);
        call.enqueue(new Callback<UpdateLoadingCompleteMilestoneResponse>() {
            @Override
            public void onResponse(Call<UpdateLoadingCompleteMilestoneResponse> call, Response<UpdateLoadingCompleteMilestoneResponse> response) {
                //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                UpdateLoadingCompleteMilestoneResponse getLoadingDetailOnVehicleDetailResponse = response.body();
                if (getLoadingDetailOnVehicleDetailResponse != null) {

                    updateLoadingCompleteMutable.setValue(getLoadingDetailOnVehicleDetailResponse);
                }
            }

            @Override
            public void onFailure(Call<UpdateLoadingCompleteMilestoneResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(application.getApplicationContext(), "Network error,\n Please check Network!!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(application.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Utils.showCustomDialog(application.getApplicationContext(),t.toString());
                }
            }
        });
        return updateLoadingCompleteMutable;
    }


    //new ExitClearance
    private MutableLiveData<ExitClearanceInvoicingResponse> getExitClearanceInvoiceLiveData = new MutableLiveData<>();

    public MutableLiveData<ExitClearanceInvoicingResponse> getExitClearanceInvoiceMutableLiveData
            (ExitClearanceNewActivity exitClearanceNewActivity, String baseUrl, int requestID, String vrn, String rfid) {
        this.baseUrl = baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<ExitClearanceInvoicingResponse> call = apiService.GetInvoiceDetailOnVehicleDetail(requestID, vrn, rfid);
        call.enqueue(new Callback<ExitClearanceInvoicingResponse>() {
            @Override
            public void onResponse(Call<ExitClearanceInvoicingResponse> call, Response<ExitClearanceInvoicingResponse> response) {
                if (response.isSuccessful()) {
                    ExitClearanceInvoicingResponse resultResponse = response.body();
                    if (resultResponse != null) {
                        // Handle successful response
                        getExitClearanceInvoiceLiveData.setValue(resultResponse);
                    }
                } else if (response.code() == 400 || response.code() == 404) {
                    try {
                        // Parse error response body as JSON
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        String errorMessage = errorBody.getString("errorMessage");
                        // Handle error message
                        //Toast.makeText(application.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                        Utils.showCustomDialog(exitClearanceNewActivity,errorMessage);
                        getExitClearanceInvoiceLiveData.setValue(null);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        // Handle error while parsing or getting error message
                        Toast.makeText(exitClearanceNewActivity, "Error processing error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle other error cases
                    Toast.makeText(exitClearanceNewActivity, "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            /*    @Override
                public void onResponse(Call<ExitClearanceInvoicingResponse> call, Response<ExitClearanceInvoicingResponse> response) {
                    //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                    ExitClearanceInvoicingResponse resultResponse=response.body();
                    if(resultResponse!=null )
                    {

                        getExitClearanceInvoiceLiveData.setValue(resultResponse);
                    }
                    else if (response.code() == 400) {

                        Toast.makeText(application.getApplicationContext(), resultResponse.getErrorMessage().toString(),Toast.LENGTH_SHORT).show();
                        // Handle HTTP 400 error
                        // Create a new ExitClearanceInvoicingResponse with error message
                    *//*    ExitClearanceInvoicingResponse errorResponse = new ExitClearanceInvoicingResponse(
                            resultResponse.getDriverCode(),resultResponse.getDriverName(),resultResponse.getErrorMessage(),resultResponse.getInvoiceDetail(),
                            resultResponse.getJobMilestoneId(),resultResponse.getMilestoneCode(),resultResponse.getMilestoneStatus(),resultResponse.getStatus(),
                            resultResponse.getVehicelTransactionId(),resultResponse.getVrn());
                    errorResponse.setErrorMessage("Bad request: " + response.message());
                    getExitClearanceInvoiceLiveData.setValue(errorResponse);*//*
                }
            }*/
            @Override
            public void onFailure(Call<ExitClearanceInvoicingResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(exitClearanceNewActivity, "Network error,\n Please check Network!!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(application.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Utils.showCustomDialog(exitClearanceNewActivity,t.toString());
                }


            }
        });
        return getExitClearanceInvoiceLiveData;
    }

    //update the ExitInvoice list
    private MutableLiveData<ExitInvoiceUpdateListResponse> updateExitClearanceInvoiceLiveData = new MutableLiveData<>();

    public MutableLiveData<ExitInvoiceUpdateListResponse> updateExitClearanceInvoiceMutableLiveData(ExitClearanceNewActivity exitClearanceNewActivity, String baseUrl, ExitClearanceInvoicingResponse exitClearanceInvoicingResponse) {
        this.baseUrl = baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<ExitInvoiceUpdateListResponse> call = apiService.UpdateInvoiceDetailOnVehicleDetail(exitClearanceInvoicingResponse);
        call.enqueue(new Callback<ExitInvoiceUpdateListResponse>() {
            @Override
            public void onResponse(Call<ExitInvoiceUpdateListResponse> call, Response<ExitInvoiceUpdateListResponse> response) {
                //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                ExitInvoiceUpdateListResponse getLoadingDetailOnVehicleDetailResponse = response.body();
                if (getLoadingDetailOnVehicleDetailResponse != null) {
                    updateExitClearanceInvoiceLiveData.setValue(getLoadingDetailOnVehicleDetailResponse);
                } else if (response.code() == 400 || response.code() == 404) {
                    try {
                        // Parse error response body as JSON
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        String errorMessage = errorBody.getString("errorMessage");
                        String statusCode = errorBody.getString("statusCode");
                        // Handle error message
                        //Toast.makeText(application.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                        Utils.showCustomDialog(exitClearanceNewActivity, errorMessage);
                        updateExitClearanceInvoiceLiveData.setValue(new ExitInvoiceUpdateListResponse(errorMessage,
                                "", false, "",
                                Integer.parseInt(statusCode)));
                        //Utils.showCustomDialogFinish(application.getApplicationContext(),errorMessage );
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Log.d("exceotuinFromresponse", e.getMessage());
                        // Handle error while parsing or getting error message
                        Toast.makeText(exitClearanceNewActivity, "Error processing error response", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ExitInvoiceUpdateListResponse> call, Throwable t) {
                Log.d("exceotuinFromFailiure", t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(exitClearanceNewActivity, "Network error,\n Please check Network!!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(application.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Utils.showCustomDialog(exitClearanceNewActivity,t.toString());

                }
            }
        });
        return updateExitClearanceInvoiceLiveData;
    }



    ///Exit clearance product verification
    private MutableLiveData<ExitClearanceProductVerificationResponse> getExitClearanceProductVerificationResponse = new MutableLiveData<>();


    public MutableLiveData<ExitClearanceProductVerificationResponse> getExitClearanceProductVerification
            (ExitClearanceNewActivity exitClearanceNewActivity, String baseUrl, int requestID, String vrn, String rfid) {
        this.baseUrl = baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<ExitClearanceProductVerificationResponse> call = apiService.GetExitClearanceProductVerification(requestID, vrn, rfid);
        call.enqueue(new Callback<ExitClearanceProductVerificationResponse>() {
            @Override
            public void onResponse(Call<ExitClearanceProductVerificationResponse> call, Response<ExitClearanceProductVerificationResponse> response) {
                if (response.isSuccessful()) {
                    ExitClearanceProductVerificationResponse resultResponse = response.body();
                    if (resultResponse != null) {
                        // Handle successful response
                        getExitClearanceProductVerificationResponse.setValue(resultResponse);
                    }
                } else if (response.code() == 400 || response.code() == 404) {
                    try {
                        // Parse error response body as JSON
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        String errorMessage = errorBody.getString("errorMessage");
                        // Handle error message
                        //Toast.makeText(application.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                        Utils.showCustomDialog(exitClearanceNewActivity,errorMessage);
                        getExitClearanceProductVerificationResponse.setValue(null);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        // Handle error while parsing or getting error message
                        Toast.makeText(exitClearanceNewActivity, "Error processing error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle other error cases
                    Toast.makeText(exitClearanceNewActivity, "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            /*    @Override
                public void onResponse(Call<ExitClearanceInvoicingResponse> call, Response<ExitClearanceInvoicingResponse> response) {
                    //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                    ExitClearanceInvoicingResponse resultResponse=response.body();
                    if(resultResponse!=null )
                    {

                        getExitClearanceInvoiceLiveData.setValue(resultResponse);
                    }
                    else if (response.code() == 400) {

                        Toast.makeText(application.getApplicationContext(), resultResponse.getErrorMessage().toString(),Toast.LENGTH_SHORT).show();
                        // Handle HTTP 400 error
                        // Create a new ExitClearanceInvoicingResponse with error message
                    *//*    ExitClearanceInvoicingResponse errorResponse = new ExitClearanceInvoicingResponse(
                            resultResponse.getDriverCode(),resultResponse.getDriverName(),resultResponse.getErrorMessage(),resultResponse.getInvoiceDetail(),
                            resultResponse.getJobMilestoneId(),resultResponse.getMilestoneCode(),resultResponse.getMilestoneStatus(),resultResponse.getStatus(),
                            resultResponse.getVehicelTransactionId(),resultResponse.getVrn());
                    errorResponse.setErrorMessage("Bad request: " + response.message());
                    getExitClearanceInvoiceLiveData.setValue(errorResponse);*//*
                }
            }*/
            @Override
            public void onFailure(Call<ExitClearanceProductVerificationResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(exitClearanceNewActivity, "Network error,\n Please check Network!!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(application.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Utils.showCustomDialog(exitClearanceNewActivity,t.toString());
                }


            }
        });
        return getExitClearanceProductVerificationResponse;
    }

    //update the Exit product list
    private MutableLiveData<ExitInvoiceUpdateListResponse> updateProductDetailOnVehicleDetail = new MutableLiveData<>();

    public MutableLiveData<ExitInvoiceUpdateListResponse> updateProductDetailOnVehicleDetailMutableLiveData(ExitClearanceNewActivity exitClearanceNewActivity, String baseUrl, ExitClearanceProductVerificationResponse exitClearanceProductVerificationResponse) {
        this.baseUrl = baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<ExitInvoiceUpdateListResponse> call = apiService.UpdateInvoiceDetailOnVehicleDetail(exitClearanceProductVerificationResponse);
        call.enqueue(new Callback<ExitInvoiceUpdateListResponse>() {
            @Override
            public void onResponse(Call<ExitInvoiceUpdateListResponse> call, Response<ExitInvoiceUpdateListResponse> response) {
                //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                ExitInvoiceUpdateListResponse getLoadingDetailOnVehicleDetailResponse = response.body();
                if (getLoadingDetailOnVehicleDetailResponse != null) {
                    updateProductDetailOnVehicleDetail.setValue(getLoadingDetailOnVehicleDetailResponse);
                } else if (response.code() == 400 || response.code() == 404) {
                    try {
                        // Parse error response body as JSON
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        String errorMessage = errorBody.getString("errorMessage");
                        String statusCode = errorBody.getString("statusCode");
                        // Handle error message
                        //Toast.makeText(application.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                        Utils.showCustomDialog(exitClearanceNewActivity, errorMessage);
                        updateProductDetailOnVehicleDetail.setValue(new ExitInvoiceUpdateListResponse(errorMessage,
                                "", false, "",
                                Integer.parseInt(statusCode)));
                        //Utils.showCustomDialogFinish(application.getApplicationContext(),errorMessage );
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Log.d("exceotuinFromresponse", e.getMessage());
                        // Handle error while parsing or getting error message
                        Toast.makeText(exitClearanceNewActivity, "Error processing error response", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ExitInvoiceUpdateListResponse> call, Throwable t) {
                Log.d("exceotuinFromFailiure", t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(exitClearanceNewActivity, "Network error,\n Please check Network!!", Toast.LENGTH_SHORT).show();
                } else {
                    Utils.showCustomDialog(exitClearanceNewActivity,t.toString());
                }
            }
        });
        return updateProductDetailOnVehicleDetail;
    }

    //update the ExitClearance physical check list
    private MutableLiveData<GeneralResponse> updateExitClearancePhysicalLiveData = new MutableLiveData<>();

    public MutableLiveData<GeneralResponse> updateExitClearancePhysicalMutableLiveData(String baseUrl, ExitClearanceInvoicingResponse exitClearanceInvoicingResponse) {
        this.baseUrl = baseUrl;
        ApiInterface apiService = APiClient.getClient(baseUrl).create(ApiInterface.class);

        Call<GeneralResponse> call = apiService.UpdatePhysicalCheckList(exitClearanceInvoicingResponse);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                //Log.d("listOfAnime",response.body().getData().get(0).getAlternativeTitles().toString());
                GeneralResponse getLoadingDetailOnVehicleDetailResponse = response.body();
                if (getLoadingDetailOnVehicleDetailResponse != null) {
                    updateExitClearancePhysicalLiveData.setValue(getLoadingDetailOnVehicleDetailResponse);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(application.getApplicationContext(), "Network error,\n Please check Network!!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(application.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Utils.showCustomDialog(application.getApplicationContext(),t.toString());
                }
            }
        });
        return updateExitClearancePhysicalLiveData;
    }


}