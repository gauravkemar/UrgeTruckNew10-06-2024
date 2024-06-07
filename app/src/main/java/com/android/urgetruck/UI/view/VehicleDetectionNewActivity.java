package com.android.urgetruck.UI.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.urgetruck.R;

import com.android.urgetruck.UI.ExitClearanceActivity;
import com.android.urgetruck.UI.HomeActivity;
import com.android.urgetruck.UI.Models.PostRfidModel;
import com.android.urgetruck.UI.Models.PostRfidResultModel;
import com.android.urgetruck.UI.Models.WBListResultModel;
import com.android.urgetruck.UI.Models.WBResponseModel;
import com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationlist.GetLocationListResponse;
import com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationmasterdatabylocationId.GetLocationMasterDataByLocationIdResponse;
import com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationlist.Location;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.Utils;
import com.android.urgetruck.UI.VehicleDetectionActivity;
import com.android.urgetruck.databinding.ActivityVehicleDetectionNewBinding;
import com.android.urgetruck.databinding.ScanLayoutBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.barcode.Scanner;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.api3.TriggerInfo;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleDetectionNewActivity extends AppCompatActivity implements RfidEventsListener {


    ActivityVehicleDetectionNewBinding binding;
    private Readers readers;
    private RFIDReader reader;
    private List<String> TagDataSet;
    private AutoCompleteTextView tvRfid;
    private TextInputLayout tv_rfid, textInputLayout_vehicleno;
    private RadioButton rbScanRfid, rbVrn;
    private TextInputLayout textInputLayoutreasons;
    private MediaPlayer mediaPlayer;
    private RadioGroup rgVehicleDetails;
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

    private int selectedParentLocationId = 0;
    private int selectedChildLocationId = 0;
    private int selectedChild2LocationId = 0;
    private Boolean checkstate = true;
    private TextInputEditText tvVrn;
    TextView toolbarText;
    ScanLayoutBinding scanLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_detection_new);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        parentLocationMapping = new HashMap<>();
        childLocationMapping = new HashMap<>();
        child2LocationMapping = new HashMap<>();
        parentLocation = new ArrayList<>();
        childLocation = new ArrayList<>();
        child2Location = new ArrayList<>();
        getParentLocationDefaultData();
        scanLayoutBinding = binding.scanLayout;
        TagDataSet = new ArrayList<>();
        connectReader();
        textInputLayoutreasons = binding.textInputLayoutreasons;
        tvRfid = scanLayoutBinding.autoCompleteTextViewRfid;
        tv_rfid = scanLayoutBinding.tvRfid;
        rgVehicleDetails = scanLayoutBinding.rgVehicleDetails;
        rbScanRfid = scanLayoutBinding.rbScanRfid;
        textInputLayout_vehicleno = scanLayoutBinding.textInputLayoutVehicleno;
        rbVrn = scanLayoutBinding.rbVrn;
        tvVrn = scanLayoutBinding.tvVrn;
        rgVehicleDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbScanRfid) {
                    textInputLayout_vehicleno.setVisibility(View.GONE);
                    tvVrn.setText("");
                    tv_rfid.setError("");
                    tv_rfid.setVisibility(View.VISIBLE);
                    checkstate = true;

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbVrn) {
                    textInputLayout_vehicleno.setError("");
                    textInputLayout_vehicleno.setVisibility(View.VISIBLE);
                    tvRfid.setText("");
                    tv_rfid.setVisibility(View.GONE);
                    checkstate = false;
                }
            }
        });
        binding.btnVehicledetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput(view);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (reader != null) {
            try {
                reader.disconnect();
                reader = null;
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (reader == null) {
            connectReader();
        }
    }

    private void initToolbar() {
        toolbarText = binding.layoutToolbar.findViewById(R.id.toolbarText);
        toolbarText.setText("Vehicle Detection");
        mediaPlayer = MediaPlayer.create(this, R.raw.scanner_sound);
        ImageView ivLogo = binding.layoutToolbar.findViewById(R.id.ivLogoLeftToolbar);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.ut_logo_with_outline);
        ivLogo.setOnClickListener(view -> {
            startActivity(new Intent(VehicleDetectionNewActivity.this, HomeActivity.class));
            finishAffinity();
        });
    }

    private void connectReader() {

        try {
            readers = new Readers(this, ENUM_TRANSPORT.SERVICE_SERIAL);
            ArrayList<ReaderDevice> readerDevices = readers.GetAvailableRFIDReaderList();

            if (!readerDevices.isEmpty()) {
                reader = readerDevices.get(0).getRFIDReader();
                reader.connect();
                Antennas.AntennaRfConfig antennaRfConfig = reader.Config.Antennas.getAntennaRfConfig(1);
                antennaRfConfig.setrfModeTableIndex(0);
                antennaRfConfig.setTari(0);
                antennaRfConfig.setTransmitPowerIndex(Integer.parseInt(Utils.getSharedPreferences(VehicleDetectionNewActivity.this, "antennapower")));
                // set the configuration
                reader.Config.Antennas.setAntennaRfConfig(1, antennaRfConfig);
                reader.Events.addEventsListener(this);
                reader.Events.setHandheldEvent(true);
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(true);
                TriggerInfo triggerInfo = new TriggerInfo();

                triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
                triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);

                //textView.setText("press trigger to scan");
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
            Toast.makeText(this, "error connecting to scanner", Toast.LENGTH_LONG).show();
        }
    }

    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
        try {
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {

                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  textView.setText("Scanning");
                            // Toast.makeText(MainActivity.this,"Looking for Tag",Toast.LENGTH_SHORT).show();
                        }
                    });

                    // start scanning

                    reader.Actions.Inventory.perform();
                } else if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                    reader.Actions.Inventory.stop();
                }
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eventReadNotify(RfidReadEvents rfidReadEvents) {
        try {
            TagData detectedTag = rfidReadEvents.getReadEventData().tagData;
            mediaPlayer.start();
            if (detectedTag != null) {

                reader.Actions.Inventory.stop();
                String tagID = detectedTag.getTagID();

                if (!TagDataSet.contains(tagID))
                    TagDataSet.add(tagID);

                ArrayAdapter<String> adapter1 =
                        new ArrayAdapter<>(
                                this,
                                R.layout.dropdown_menu_popup_item,
                                TagDataSet);
                Spinner spinner = scanLayoutBinding.spinner;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (TagDataSet.size() == 1) {
                            tvRfid.setText(adapter1.getItem(0).toString(), false);
                        } else {
                            tvRfid.setText("");
                            tv_rfid.setError("Select the RFID value from dropdown");
                        }
                        tvRfid.setAdapter(adapter1);
                        spinner.setAdapter(adapter1);
                    }
                });

               /* else {
                    String tagID = detectedTag.getTagID();
                    exitClearanceNewAdapter.markCheckboxByTag(tagID);
                    exitClearanceNewAdapter.notifyDataSetChanged();
                }*/
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        }
    }

    private void getParentLocationDefaultData() {
        try {
            if (Utils.isConnected(this)) {
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                String baseurl = Utils.getSharedPreferences(VehicleDetectionNewActivity.this, "apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
                String parentLocationCode = "";
                Call<GetLocationListResponse> call = apiService.getVehicleLocationDefaultList(123456789, "null");
                call.enqueue(new Callback<GetLocationListResponse>() {
                    @Override
                    public void onResponse(Call<GetLocationListResponse> call, Response<GetLocationListResponse> response) {
                        parentLocation.clear();
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        try {
                            if (response.isSuccessful()) {
                                GetLocationListResponse locationModel = response.body();
                                parentLocationsModel = locationModel.getLocations();
                                for (Location location : parentLocationsModel) {
                                    parentLocation.add(location.getDisplayName());
                                    addToParentLocationCoordinatesMap(location.getDisplayName(), location.getLocationCode());
                                }
                                populateParentLocationDropdown(parentLocation);
                            } else {
                                PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                                //Log.e("msg", message.getStatusMessage());
                                Utils.showCustomDialogFinish(VehicleDetectionNewActivity.this, message.getStatusMessage());

                            }

                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Exception : No Data Found");
                        }


                    }

                    @Override
                    public void onFailure(Call<GetLocationListResponse> call, Throwable t) {
                        Log.d("TAG", "Response = " + t.toString());
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        // Utils.showCustomDialogFinish(VehicleDetectionActivity.this,t.toString());
                        binding.textInputLayoutlocation.setVisibility(View.GONE);
                        binding.textInputLayoutChild3.setVisibility(View.GONE);
                        binding.textInputLayoutChild2.setVisibility(View.GONE);
                        try {
                            if (t instanceof SocketTimeoutException) {
                                // Handle timeout exception with custom message
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Network error,\n Please check Network!!");
                            } else {
                                // Handle other exceptions
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, t.toString());
                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Exception : No Data Found");
                        }


                    }
                });

            } else {
                Utils.showCustomDialogFinish(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            Utils.showCustomDialog(this, e.getMessage());
        }

    }

    private void getChildLocationDefaultData(String parentLocationCode) {
        try {
            if (Utils.isConnected(this)) {
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                String baseurl = Utils.getSharedPreferences(VehicleDetectionNewActivity.this, "apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
                Call<GetLocationListResponse> call = apiService.getVehicleLocationList(123456789, parentLocationCode);
                call.enqueue(new Callback<GetLocationListResponse>() {
                    @Override
                    public void onResponse(Call<GetLocationListResponse> call, Response<GetLocationListResponse> response) {
                        childLocation.clear();
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        binding.textInputLayoutChild3.setVisibility(View.GONE);
                        try
                        {
                            if (response.isSuccessful()) {
                                GetLocationListResponse locationModel = response.body();
                                childLocationModel = locationModel.getLocations();
                                for (Location location : childLocationModel) {
                                    childLocation.add(location.getDisplayName());
                                    addToChildLocationCoordinatesMap(location.getDisplayName(), String.valueOf(location.getLocationId()));
                                }
                                if (childLocation.size() > 0) {
                                    binding.textInputLayoutChild2.setVisibility(View.VISIBLE);
                                    populateChildDropdown(childLocation);
                                } else {
                                    binding.textInputLayoutChild3.setVisibility(View.GONE);
                                    binding.textInputLayoutChild2.setVisibility(View.GONE);
                                }
                            }
                        }catch(Exception e)
                        {
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Exception : No Data Found");
                        }

                    }

                    @Override
                    public void onFailure(Call<GetLocationListResponse> call, Throwable t) {
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        binding.textInputLayoutChild3.setVisibility(View.GONE);
                        binding.textInputLayoutChild2.setVisibility(View.GONE);
                        try {
                            Log.d("TAG", "Response = " + t.toString());
                            // Utils.showCustomDialogFinish(VehicleDetectionActivity.this,t.toString());
                            if (t instanceof SocketTimeoutException) {
                                // Handle timeout exception with custom message
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Network error,\n Please check Network!!");
                            } else {
                                // Handle other exceptions
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, t.toString());
                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Exception : No Data Found");
                        }
                    }
                });

            } else {

                Utils.showCustomDialogFinish(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            Utils.showCustomDialog(this, e.getMessage());
        }

    }


    private void getChild2LocationDefaultData(int locationId) {
        try {
            if (Utils.isConnected(this)) {
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                String baseurl = Utils.getSharedPreferences(VehicleDetectionNewActivity.this, "apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
                Call<ArrayList<GetLocationMasterDataByLocationIdResponse>> call = apiService.getLocationMasterDataByLocationId(123456789, locationId);
                call.enqueue(new Callback<ArrayList<GetLocationMasterDataByLocationIdResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<GetLocationMasterDataByLocationIdResponse>> call, Response<ArrayList<GetLocationMasterDataByLocationIdResponse>> response) {
                        child2Location.clear();
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        try {
                            if (response.isSuccessful()) {
                                ArrayList<GetLocationMasterDataByLocationIdResponse> locationModel = response.body();
                                child2LocationModel = locationModel;
                                for (GetLocationMasterDataByLocationIdResponse location : child2LocationModel) {
                                    child2Location.add(location.getDeviceName());
                                    addToChild2LocationCoordinatesMap(String.valueOf(location.getDeviceLocationMappingId()), location.getDeviceName());
                                }
                                if (child2Location.size() > 0) {
                                    binding.textInputLayoutChild3.setVisibility(View.VISIBLE);
                                    populateChild2Dropdown(child2Location);
                                } else {
                                    binding.textInputLayoutChild3.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Exception : No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<GetLocationMasterDataByLocationIdResponse>> call, Throwable t) {
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        try {
                            Log.d("TAG", "Response = " + t.toString());
                            // Utils.showCustomDialogFinish(VehicleDetectionActivity.this,t.toString());
                            if (t instanceof SocketTimeoutException) {
                                // Handle timeout exception with custom message
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Network error,\n Please check Network!!");
                            } else {
                                // Handle other exceptions
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, t.toString());
                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Exception : No Data Found");
                        }

                    }
                });

            } else {

                Utils.showCustomDialogFinish(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            Utils.showCustomDialog(this, e.getMessage());
        }

    }

    private void setToDefault() {
        getParentLocationDefaultData();
        selectedChild2LocationId = 0;
        binding.textInputLayoutChild2.setVisibility(View.GONE);
        binding.textInputLayoutChild3.setVisibility(View.GONE);
        binding.autoCompleteTextViewReason.setText("");
        tvRfid.setText("");
        binding.autoCompleteTextViewLocation.setText("");
        TagDataSet.clear();

    }

    private void callPostRfidApi() {

        binding.progressbar.setVisibility(View.VISIBLE);

        try {
            if (Utils.isConnected(this)) {
                String baseurl = Utils.getSharedPreferences(VehicleDetectionNewActivity.this, "apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);

                PostRfidModel modal;

                if (checkstate) {
                    modal = new PostRfidModel("123456", tvRfid.getText().toString().trim(), String.valueOf(selectedChild2LocationId), "", binding.autoCompleteTextViewReason.getText().toString().trim());

                } else {
                    modal = new PostRfidModel("123456", "", String.valueOf(selectedChild2LocationId), tvVrn.getText().toString().trim(), binding.autoCompleteTextViewReason.getText().toString().trim());
                }


                Log.e("Request", new Gson().toJson(modal));

                Call<PostRfidResultModel> call = apiService.PostRfid(modal);

                call.enqueue(new Callback<PostRfidResultModel>() {
                    @Override
                    public void onResponse(Call<PostRfidResultModel> call, Response<PostRfidResultModel> response) {
                        binding.progressbar.setVisibility(View.GONE);
                        try {
                            if (response.isSuccessful()) {
                                setToDefault();
                                if (response.body().getStatusMessage() != null) {
                                    Utils.showCustomDialogFinish(VehicleDetectionNewActivity.this, response.body().getStatusMessage());
                                } else {
                                    Utils.showCustomDialogFinish(VehicleDetectionNewActivity.this, "Success");
                                    // finish();
                                }

                            } else {
                                setToDefault();
                                PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                                Log.e("msg", message.getStatusMessage());
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, message.getStatusMessage());

                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Exception : No Data Found");
                        }

                    }

                    @Override
                    public void onFailure(Call<PostRfidResultModel> call, Throwable t) {
                        binding.progressbar.setVisibility(View.GONE);
                        try {
                            Log.e("error", t.toString());
                            // Utils.showCustomDialog(VehicleDetectionActivity.this,t.toString());
                            setToDefault();
                            if (t instanceof SocketTimeoutException) {
                                // Handle timeout exception with custom message
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Network error,\n Please check Network!!");
                            } else {
                                // Handle other exceptions
                                Utils.showCustomDialog(VehicleDetectionNewActivity.this, t.toString());
                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionNewActivity.this, "Exception : No Data Found");
                        }

                    }
                });
            } else {
                Utils.showCustomDialogFinish(this, getString(R.string.internet_connection));
            }

        } catch (Exception e) {
            Utils.showCustomDialog(VehicleDetectionNewActivity.this, e.getMessage());
        }

    }

    public void confirmInput(View v) {
        if (!validateReason() || !validateLocation() || !validateRFIDorVRN()) {
            return;
        }
        if (!Utils.isConnected(VehicleDetectionNewActivity.this)) {
            Utils.showCustomDialog(VehicleDetectionNewActivity.this, getString(R.string.internet_connection));
        } else {

            callPostRfidApi();
        }
        //Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }

    private boolean validateReason() {
        String reasonInput = textInputLayoutreasons.getEditText().getText().toString().trim();

        if (reasonInput.isEmpty()) {
            textInputLayoutreasons.setError("Please Select a reason");
            return false;
        } else {
            textInputLayoutreasons.setError(null);
            return true;
        }
    }

    private boolean validateLocation() {
        if (selectedChild2LocationId == 0) {
            Toast.makeText(this, "Please Select Location", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateRFIDorVRN() {
        String scanRFIDInput = tv_rfid.getEditText().getText().toString().trim();
        String vrnInput = textInputLayout_vehicleno.getEditText().getText().toString().trim();
        if (rbScanRfid.isChecked() && scanRFIDInput.isEmpty()) {
            tv_rfid.setError("Press trigger to Scan RFID");
            return false;
        } else if (rbVrn.isChecked() && vrnInput.isEmpty()) {
            textInputLayout_vehicleno.setError("Please enter VRN");
            return false;
        } else if (rbVrn.isChecked() && vrnInput.length() < 8) {
            textInputLayout_vehicleno.setError("Please enter 8 to 10 digits VRN");

        }
        return true;


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

    public void populateParentLocationDropdown(ArrayList<String> locationDataArray) {

        populateDropdown();
      /*  ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray);*/
        parentLocationAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray);

        binding.autoCompleteTextViewLocation.setAdapter(parentLocationAdapter);

        binding.autoCompleteTextViewLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = binding.autoCompleteTextViewLocation.getText().toString();
                Integer selectedItemPosi = (adapterView != null) ? adapterView.getSelectedItemPosition() : null;
                String selectedKey = null;
                for (Map.Entry<String, String> entry : parentLocationMapping.entrySet()) {
                    if (entry.getKey().equals(selectedItem)) {
                        selectedKey = entry.getValue();
                        break;
                    }
                }
                if (selectedKey != null) {
                   // selectedParentLocationId = Integer.parseInt(selectedKey);
                    getChildLocationDefaultData( selectedKey);
                }
            }
        });

    }

    public void populateChildDropdown(ArrayList<String> locationDataArray) {
        binding.autoCompleteTextViewLocationChild2.setText("");
/*        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray);*/

        childLocationAdapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu_popup_item,
                locationDataArray);
        binding.autoCompleteTextViewLocationChild2.setAdapter(childLocationAdapter);
        binding.autoCompleteTextViewLocationChild2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = binding.autoCompleteTextViewLocationChild2.getText().toString();
                Integer selectedItemPosi = (adapterView != null) ? adapterView.getSelectedItemPosition() : null;

                /*if (!isInitialSelectChildLoc) {
                    if (!locationDataArray.get(i).equals("Select Location")) {
                        String selectedKey = null;
                        for (Map.Entry<String, String> entry : childLocationMapping.entrySet()) {
                            if (entry.getKey().equals(selectedItem)) {
                                selectedKey = entry.getValue();
                                break;
                            }
                        }
                        if (selectedKey != null) {
                            selectedChildLocationId = Integer.parseInt(selectedKey);
                            // callParentLocationApi(selectedKey);
                            getChild2LocationDefaultData(Integer.parseInt(selectedKey));
                        }
                    } else {
                        selectedChildLocationId = 0;
                    }
                }
                isInitialSelectChildLoc = false;*/

                String selectedKey = null;
                for (Map.Entry<String, String> entry : childLocationMapping.entrySet()) {
                    if (entry.getKey().equals(selectedItem)) {
                        selectedKey = entry.getValue();
                        break;
                    }
                }
                if (selectedKey != null) {
                    selectedChildLocationId = Integer.parseInt(selectedKey);
                    getChild2LocationDefaultData(Integer.parseInt(selectedKey));
                }

            }
        });
    }

    public void populateChild2Dropdown(ArrayList<String> locationDataArray) {
        binding.autoCompleteTextViewLocationChild3.setText("");

    /*    ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray); */

        child2LocationAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray);

        binding.autoCompleteTextViewLocationChild3.setAdapter(child2LocationAdapter);
        binding.autoCompleteTextViewLocationChild3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //String selectedItem = (adapterView != null) ? adapterView.getSelectedItem().toString() : "";
                String selectedItem = binding.autoCompleteTextViewLocationChild3.getText().toString();
                Integer selectedItemPosi = (adapterView != null) ? adapterView.getSelectedItemPosition() : null;

                String selectedKey = null;
                for (Map.Entry<String, String> entry : child2LocationMapping.entrySet()) {
                    if (entry.getValue().equals(selectedItem)) {
                        selectedKey = entry.getKey();
                        break;
                    }
                }
                if (selectedKey != null) {
                    selectedChild2LocationId = Integer.parseInt(selectedKey);
                }

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        try {
            if (reader != null) {
                reader = null;
                readers.Dispose();
                readers = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateDropdown() {


        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        getResources().getStringArray(R.array.vehicle_detectionreasons));

        AutoCompleteTextView editTextFilledExposedDropdown1 =
                findViewById(R.id.autoCompleteTextView_reason);


        editTextFilledExposedDropdown1.setAdapter(adapter1);
    }
   /* void callPostRfidApi() {
        binding.progressbar.setVisibility(View.VISIBLE);

        String baseurl= Utils.getSharedPreferences(VehicleDetectionNewActivity.this,"apiurl");
        ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);

        PostRfidModel modal;

        if(checkstate){
            modal =new PostRfidModel("123456",tvRfid.getText().toString().trim(),location,"",autoCompleteTextView_reason.getText().toString().trim());

        }else{
            modal = new PostRfidModel("123456","",location,tvVrn.getText().toString().trim(),autoCompleteTextView_reason.getText().toString().trim());
        }


        Log.e("Request",new Gson().toJson(modal));

        Call<PostRfidResultModel> call = apiService.PostRfid(modal);

        call.enqueue(new Callback<PostRfidResultModel>() {
            @Override
            public void onResponse(Call<PostRfidResultModel> call, Response<PostRfidResultModel> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful())
                {
                    if(response.body().getStatusMessage()!=null) {
                        Utils.showCustomDialogFinish(VehicleDetectionActivity.this, response.body().getStatusMessage());
                    }else{
                        Utils.showCustomDialogFinish(VehicleDetectionActivity.this, "Success");
                        // finish();
                    }

                }else{

                    PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                    Log.e("msg",message.getStatusMessage());
                    Utils.showCustomDialog(VehicleDetectionActivity.this,message.getStatusMessage());

                }

            }

            @Override
            public void onFailure(Call<PostRfidResultModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("error",t.toString());
                // Utils.showCustomDialog(VehicleDetectionActivity.this,t.toString());

                if (t instanceof SocketTimeoutException) {
                    // Handle timeout exception with custom message
                    Utils.showCustomDialog(VehicleDetectionActivity.this,"Network error,\n Please check Network!!");
                } else {
                    // Handle other exceptions
                    Utils.showCustomDialog(VehicleDetectionActivity.this,t.toString());
                }
            }
        });
    }*/

}