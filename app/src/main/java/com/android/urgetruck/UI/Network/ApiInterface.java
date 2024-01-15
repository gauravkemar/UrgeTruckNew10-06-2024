package com.android.urgetruck.UI.Network;

import com.android.urgetruck.UI.Models.ExitClearanceResultModel;
import com.android.urgetruck.UI.Models.GetCheckListItemsModel;
import com.android.urgetruck.UI.Models.GetExitClearanceModel;
import com.android.urgetruck.UI.Models.LocationModel;
import com.android.urgetruck.UI.Models.LoginModel;
import com.android.urgetruck.UI.Models.LoginResultModel;
import com.android.urgetruck.UI.Models.PostRfidModel;
import com.android.urgetruck.UI.Models.PostRfidResultModel;
import com.android.urgetruck.UI.Models.RfidMappingModel;
import com.android.urgetruck.UI.Models.RfidMappingResultModel;
import com.android.urgetruck.UI.Models.SecurityCheckResultModel;
import com.android.urgetruck.UI.Models.TrackVehicleModel;
import com.android.urgetruck.UI.Models.TrackVehicleResultModel;
import com.android.urgetruck.UI.Models.WBListResultModel;
import com.android.urgetruck.UI.Models.WBResponseModel;
import com.android.urgetruck.UI.Models.WeightDetailsResultModel;
import com.android.urgetruck.UI.Models.invoicecheckingstar.GetLoadingDetailOnVehicleDetailResponse;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneRequest;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    //Login

    @POST("Login")
    Call<LoginResultModel> PostRfid(@Body LoginModel loginModel);



    // Vehicle Detection
    @GET("GetLocationMasterData")
    Call<LocationModel> getLocations(@Query("RequestId") int RequestID);


    @POST("POSTRFIDTag")
    Call<PostRfidResultModel> PostRfid(@Body PostRfidModel postRfidModel);


    //Vehicle Mapping

    @POST("PostRFIDVerifyMap")
    Call<RfidMappingResultModel> RfidMapping(@Body RfidMappingModel rfidMappingModel);


    //Security Check

    @GET("GetWeighmentDetails")
    Call<WeightDetailsResultModel> getWeightDetails(@Query("RequestId") int RequestID,@Query("RFIDTagNo") String Rfid,@Query("VRN") String vrn);

    @Multipart
    @POST("PostSecurityCheckForWeighment")
    Call<SecurityCheckResultModel> postSecurityCheck(@Part List<MultipartBody.Part> files, @Part("JSONData") RequestBody name);


    // Exit Clearance

    @GET("GetExitClearanceDetails")
    Call<GetExitClearanceModel> getExitClearanceDetails(@Query("RequestId") int RequestID,@Query("RFIDTagNo") String Rfid,@Query("VRN") String vrn);


    @GET("GetAllExistCheckList")
    Call<GetCheckListItemsModel> getCheckListItems(@Query(("RequestId")) int RequestID);



    @Multipart
    @POST("PostExitClearance")
    Call<ExitClearanceResultModel> postExitClearance(@Part List<MultipartBody.Part> files, @Part("JSONData") RequestBody name);




    // Track Vehicle

    @POST("PostVehicleTrackingRequest")
    Call<TrackVehicleResultModel> getTrackVehicleDetails(@Body TrackVehicleModel trackVehicleModel);





    @GET("GetAllWeighBridgeList")
    Call<WBResponseModel> getAllWeighBridgeList();

    //new api

    @GET("GetLoadingDetailOnVehicleDetail")
    Call<GetLoadingDetailOnVehicleDetailResponse> GetLoadingDetailOnVehicleDetail(@Query("RequestId") int RequestID,
                                                                                  @Query("VRN") String vrn,
                                                                                  @Query("RFIDTagNo") String Rfid);

    @POST("PostVehicleTrackingRequest")
    Call<UpdateLoadingCompleteMilestoneResponse> updateLoadingCompleteMilestone(@Body UpdateLoadingCompleteMilestoneRequest updateLoadingCompleteMilestoneRequest);



}
