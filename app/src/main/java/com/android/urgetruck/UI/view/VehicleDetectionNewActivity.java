package com.android.urgetruck.UI.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.android.urgetruck.R;

import com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationlist.GetLocationListResponse;
import com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationmasterdatabylocationId.GetLocationMasterDataByLocationIdResponse;
import com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationlist.Location;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.Utils;
import com.android.urgetruck.databinding.ActivityVehicleDetectionNewBinding;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleDetectionNewActivity extends AppCompatActivity  {


    ActivityVehicleDetectionNewBinding binding;

    private ArrayList<String> parentLocation;
    private ArrayAdapter<String> parentLocationAdapter;

    private ArrayList<String> childLocation;
    private ArrayAdapter<String> childLocationAdapter;

    private ArrayList<String> child2Location;
    private ArrayAdapter<String> child2LocationAdapter;


    private HashMap<String, String> parentLocationMapping;
    private HashMap<String, String> childLocationMapping;
    private HashMap<String, String> child2LocationMapping;


    List<Location> parentLocationsModel;
    List<Location> childLocationModel;
    ArrayList<GetLocationMasterDataByLocationIdResponse> child2LocationModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_vehicle_detection_new);

        parentLocationMapping = new HashMap<>();
        childLocationMapping = new HashMap<>();
        child2LocationMapping = new HashMap<>();

        parentLocation = new ArrayList<>();
        childLocation = new ArrayList<>();
        child2Location = new ArrayList<>();

        getParentLocationDefaultData();
        getChildLocationDefaultData(1002);
        getChild2LocationDefaultData(8);
    }


    private void getParentLocationDefaultData() {
        try{
            if(Utils.isConnected(this)){
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                String baseurl= Utils.getSharedPreferences(VehicleDetectionNewActivity.this,"apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
                Call<GetLocationListResponse> call = apiService.getVehicleLocationDefaultList (123456789,"");
                call.enqueue(new Callback<GetLocationListResponse>() {
                    @Override
                    public void onResponse(Call<GetLocationListResponse> call, Response<GetLocationListResponse> response) {
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        GetLocationListResponse locationModel = response.body();
                        parentLocationsModel = locationModel.getLocations();
                        for( Location location : parentLocationsModel){
                            parentLocation.add(location.getDisplayName());
                            addToParentLocationCoordinatesMap(location.getDisplayName(),location.getLocationCode());
                        }
                        populateParentLocationDropdown(parentLocation);

                    }
                    @Override
                    public void onFailure(Call<GetLocationListResponse> call, Throwable t) {
                        Log.d("TAG","Response = "+t.toString());
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        // Utils.showCustomDialogFinish(VehicleDetectionActivity.this,t.toString());

                        if (t instanceof SocketTimeoutException) {
                            // Handle timeout exception with custom message
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this,"Network error,\n Please check Network!!");
                        } else {
                            // Handle other exceptions
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this,t.toString());
                        }

                    }
                });

            }else{
                Utils.showCustomDialogFinish(this,getString(R.string.internet_connection));
            }
        }
        catch (Exception e)
        {
            Utils.showCustomDialogFinish(this,e.getMessage());
        }

    }

    private void getChildLocationDefaultData(int parentLocationCode) {
        try{
            if(Utils.isConnected(this)){
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                String baseurl= Utils.getSharedPreferences(VehicleDetectionNewActivity.this,"apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
                Call<GetLocationListResponse> call = apiService.getVehicleLocationList (123456789,parentLocationCode);
                call.enqueue(new Callback<GetLocationListResponse>() {
                    @Override
                    public void onResponse(Call<GetLocationListResponse> call, Response<GetLocationListResponse> response) {
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        GetLocationListResponse locationModel = response.body();
                        childLocationModel = locationModel.getLocations();
                        for( Location location : childLocationModel){
                            childLocation.add(location.getDisplayName());
                            addToChildLocationCoordinatesMap(location.getDisplayName(),String.valueOf(location.getLocationId()));
                        }
                        populateChildDropdown(childLocation);

                    }
                    @Override
                    public void onFailure(Call<GetLocationListResponse> call, Throwable t) {
                        Log.d("TAG","Response = "+t.toString());
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        // Utils.showCustomDialogFinish(VehicleDetectionActivity.this,t.toString());

                        if (t instanceof SocketTimeoutException) {
                            // Handle timeout exception with custom message
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this,"Network error,\n Please check Network!!");
                        } else {
                            // Handle other exceptions
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this,t.toString());
                        }

                    }
                });

            }else{
                Utils.showCustomDialogFinish(this,getString(R.string.internet_connection));
            }
        }
        catch (Exception e)
        {
            Utils.showCustomDialogFinish(this,e.getMessage());
        }

    }


    private void getChild2LocationDefaultData(int locationId) {
        try{
            if(Utils.isConnected(this)){
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                String baseurl= Utils.getSharedPreferences(VehicleDetectionNewActivity.this,"apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
                Call<ArrayList<GetLocationMasterDataByLocationIdResponse>> call = apiService.getLocationMasterDataByLocationId (123456789,locationId);
                call.enqueue(new Callback<ArrayList<GetLocationMasterDataByLocationIdResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<GetLocationMasterDataByLocationIdResponse>> call, Response<ArrayList<GetLocationMasterDataByLocationIdResponse>> response) {
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        ArrayList<GetLocationMasterDataByLocationIdResponse> locationModel = response.body();
                        child2LocationModel = locationModel;
                        for( GetLocationMasterDataByLocationIdResponse location : child2LocationModel){
                            child2Location.add(location.getDeviceName());
                            addToChild2LocationCoordinatesMap(location.getDeviceName(),String.valueOf(location.getLocationId()));
                        }
                        populateChild2Dropdown(child2Location);

                    }
                    @Override
                    public void onFailure(Call<ArrayList<GetLocationMasterDataByLocationIdResponse>> call, Throwable t) {
                        Log.d("TAG","Response = "+t.toString());
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        // Utils.showCustomDialogFinish(VehicleDetectionActivity.this,t.toString());

                        if (t instanceof SocketTimeoutException) {
                            // Handle timeout exception with custom message
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this,"Network error,\n Please check Network!!");
                        } else {
                            // Handle other exceptions
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this,t.toString());
                        }

                    }
                });

            }else{
                Utils.showCustomDialogFinish(this,getString(R.string.internet_connection));
            }
        }
        catch (Exception e)
        {
            Utils.showCustomDialogFinish(this,e.getMessage());
        }

    }



    public void addToParentLocationCoordinatesMap(String key, String value) {
        parentLocationMapping.put(key, value);
    }
    public void addToChildLocationCoordinatesMap(String key, String value) {
        childLocationMapping.put(key, value);
    }
    public void addToChild2LocationCoordinatesMap(String key, String value) {
        child2LocationMapping.put(key, value);
    }





    public void populateParentLocationDropdown(ArrayList<String> locationDataArray){


       ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray);

        AutoCompleteTextView editTextFilledExposedDropdown1 =
                findViewById(R.id.autoCompleteTextView_location);


        editTextFilledExposedDropdown1.setAdapter(adapter);
    }

    public void populateChildDropdown(ArrayList<String> locationDataArray){

       ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray);

        AutoCompleteTextView editTextFilledExposedDropdown1 =
                findViewById(R.id.autoCompleteTextView_reason);

        editTextFilledExposedDropdown1.setAdapter(adapter1);
    }

    public void populateChild2Dropdown(ArrayList<String> locationDataArray){


       ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray);

        AutoCompleteTextView editTextFilledExposedDropdown1 =
                findViewById(R.id.autoCompleteTextView_location_child3);


        editTextFilledExposedDropdown1.setAdapter(adapter1);
    }

    public void populateDropdown( ){


       ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        getResources().getStringArray(R.array.vehicle_detectionreasons));

        AutoCompleteTextView editTextFilledExposedDropdown1 =
                findViewById(R.id.autoCompleteTextView_reason);


        editTextFilledExposedDropdown1.setAdapter(adapter1);
    }

}