package com.android.urgetruck.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.Location;
import com.android.urgetruck.UI.Models.LocationModel;
import com.android.urgetruck.UI.Models.PostRfidModel;
import com.android.urgetruck.UI.Models.PostRfidResultModel;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleDetectionActivity extends AppCompatActivity implements RfidEventsListener {

    private Readers readers;
    private RFIDReader reader;
    private List<String> TagDataSet;
    private ProgressBar progressBar;
    private AutoCompleteTextView autoCompleteTextView_reason;
    private AutoCompleteTextView autoCompleteTextView_location;
    private TextInputLayout textInputLayoutreasons, textInputLayoutlocation;
    private String location, reason;
    List<Location> locations;
    private AutoCompleteTextView tvRfid;
    private TextInputLayout tv_rfid, textInputLayout_vehicleno;
    private RadioGroup rgVehicleDetails;
    private RadioButton rbScanRfid, rbVrn;
    private TextInputEditText tvVrn;
    private Boolean checkstate = true;
    View layout_toolbar;
    TextView toolbarText;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanrfid);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        layout_toolbar = findViewById(R.id.layout_toolbar);
        initToolbar();
        //getSupportActionBar().setTitle(getResources().getText(R.string.vehicle_detection_activity));
        progressBar = findViewById(R.id.progressbar);
        autoCompleteTextView_location = findViewById(R.id.autoCompleteTextView_location);
        autoCompleteTextView_reason = findViewById(R.id.autoCompleteTextView_reason);
        textInputLayoutlocation = findViewById(R.id.textInputLayoutlocation);
        textInputLayoutreasons = findViewById(R.id.textInputLayoutreasons);
        tvRfid = findViewById(R.id.autoCompleteTextView_rfid);
        tv_rfid = findViewById(R.id.tv_rfid);
        textInputLayout_vehicleno = findViewById(R.id.textInputLayout_vehicleno);
        rgVehicleDetails = findViewById(R.id.rgVehicleDetails);
        rbScanRfid = findViewById(R.id.rbScanRfid);
        rbVrn = findViewById(R.id.rbVrn);
        tvVrn = findViewById(R.id.tvVrn);
        getLocationData();

        autoCompleteTextView_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                location = locations.get(i).getDeviceLocationId().toString();

            }
        });

        TagDataSet = new ArrayList<>();
        connectReader();
        findViewById(R.id.btn_vehicledetection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput(view);

            }
        });
//        tvRfid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(tvRfid.getText().toString().length()==0){
//                    tv_rfid.setError("Please press trigger to scan rfid");
//                   // Toast.makeText(VehicleDetectionActivity.this,"Please press trigger to scan rfid",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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

    }

    private void initToolbar() {
        mediaPlayer = MediaPlayer.create(this, R.raw.scanner_sound);
        toolbarText = layout_toolbar.findViewById(R.id.toolbarText);
        toolbarText.setText(getString(R.string.vehicle_detection_activity));

        ImageView ivLogo = layout_toolbar.findViewById(R.id.ivLogoLeftToolbar);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.ut_logo_with_outline);
        ivLogo.setOnClickListener(view -> {
            startActivity(new Intent(VehicleDetectionActivity.this, HomeActivity.class));
            finishAffinity();
        });
    }

    private void callPostRfidApi() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            if (Utils.isConnected(this))
            {
                String baseurl = Utils.getSharedPreferences(VehicleDetectionActivity.this, "apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);

                PostRfidModel modal;

                if (checkstate) {
                    modal = new PostRfidModel("123456", tvRfid.getText().toString().trim(), location, "", autoCompleteTextView_reason.getText().toString().trim());

                }
                else {
                    modal = new PostRfidModel("123456", "", location, tvVrn.getText().toString().trim(), autoCompleteTextView_reason.getText().toString().trim());
                }


                Log.e("Request", new Gson().toJson(modal));

                Call<PostRfidResultModel> call = apiService.PostRfid(modal);

                call.enqueue(new Callback<PostRfidResultModel>() {
                    @Override
                    public void onResponse(Call<PostRfidResultModel> call, Response<PostRfidResultModel> response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            if (response.isSuccessful()) {
                                if (response.body().getStatusMessage() != null) {
                                    Utils.showCustomDialogFinish(VehicleDetectionActivity.this, response.body().getStatusMessage());
                                } else {
                                    Utils.showCustomDialogFinish(VehicleDetectionActivity.this, "Success");
                                    // finish();
                                }

                            } else {

                                PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                                Log.e("msg", message.getStatusMessage());
                                Utils.showCustomDialog(VehicleDetectionActivity.this, message.getStatusMessage());

                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionActivity.this, "Exception : No Data Found");
                        }



                    }

                    @Override
                    public void onFailure(Call<PostRfidResultModel> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("error", t.toString());
                        // Utils.showCustomDialog(VehicleDetectionActivity.this,t.toString());
                        try {
                            if (t instanceof SocketTimeoutException) {
                                // Handle timeout exception with custom message
                                Utils.showCustomDialog(VehicleDetectionActivity.this, "Network error,\n Please check Network!!");
                            } else {
                                // Handle other exceptions
                                Utils.showCustomDialog(VehicleDetectionActivity.this, t.toString());
                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionActivity.this, "Exception : No Data Found");
                        }

                    }
                });
            }
            else {
                Utils.showCustomDialogFinish(this, getString(R.string.internet_connection));
            }
        }
        catch (Exception e)
        {
            Utils.showCustomDialog(this, e.getMessage());
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (reader == null) {
            connectReader();
        }
    }

    private void getLocationData() {
        try {
            if (Utils.isConnected(this)) {
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                String baseurl = Utils.getSharedPreferences(VehicleDetectionActivity.this, "apiurl");
                ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
                Call<LocationModel> call = apiService.getLocations(123456789);
                call.enqueue(new Callback<LocationModel>() {
                    @Override
                    public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {

                        try {
                            LocationModel locationModel = response.body();
                            locations = locationModel.getLocations();
                            ArrayList<String> locationDataArray = new ArrayList<>();
                            for (Location location : locations) {
                                locationDataArray.add(location.getDeviceName());

                            }
                            populateDropdown(locationDataArray);
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionActivity.this, "Exception : No Data Found");
                        }


                        findViewById(R.id.progressbar).setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<LocationModel> call, Throwable t) {
                        Log.d("TAG", "Response = " + t.toString());
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        // Utils.showCustomDialogFinish(VehicleDetectionActivity.this,t.toString());
                        try {
                            if (t instanceof SocketTimeoutException) {
                                // Handle timeout exception with custom message
                                Utils.showCustomDialog(VehicleDetectionActivity.this, "Network error,\n Please check Network!!");
                            } else {
                                // Handle other exceptions
                                Utils.showCustomDialog(VehicleDetectionActivity.this, t.toString());
                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(VehicleDetectionActivity.this, "Exception : No Data Found");
                        }


                    }
                });

            } else {
                Utils.showCustomDialogFinish(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            Utils.showCustomDialogFinish(this, e.getMessage());
        }


    }

    public void populateDropdown(ArrayList<String> locationDataArray) {

        ArrayAdapter<String> adapter;
        adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationDataArray);

        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.autoCompleteTextView_location);
        editTextFilledExposedDropdown.setAdapter(adapter);


        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        getResources().getStringArray(R.array.vehicle_detectionreasons));

        AutoCompleteTextView editTextFilledExposedDropdown1 =
                findViewById(R.id.autoCompleteTextView_reason);


        editTextFilledExposedDropdown1.setAdapter(adapter1);
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


                Spinner spinner = findViewById(R.id.spinner);

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
                        // editTextFilledExposedDropdown3.setText(adapter1.getItem(0));
                        // editTextFilledExposedDropdown3.isPopupShowing();
                        spinner.setAdapter(adapter1);

//                        for (String i : TagDataSet) {
//                            Toast.makeText(ScanrfidActivity.this,i,Toast.LENGTH_SHORT).show();
//
//                        }


                    }
                });


            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        }

    }

    @Override
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
                antennaRfConfig.setTransmitPowerIndex(Integer.parseInt(Utils.getSharedPreferences(VehicleDetectionActivity.this, "antennapower")));
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
        String locationInput = textInputLayoutlocation.getEditText().getText().toString().trim();

        if (locationInput.isEmpty()) {
            textInputLayoutlocation.setError("Please Select a location");
            return false;
        } else {
            textInputLayoutlocation.setError(null);
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

    public void confirmInput(View v) {
        if (!validateReason() || !validateLocation() || !validateRFIDorVRN()) {
            return;
        }
        if (!Utils.isConnected(VehicleDetectionActivity.this)) {
            Utils.showCustomDialog(VehicleDetectionActivity.this, getString(R.string.internet_connection));
        } else {

            callPostRfidApi();
        }
        //Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
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
}
