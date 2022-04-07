package com.android.urgetruck.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.PostRfidResultModel;
import com.android.urgetruck.UI.Models.RfidMappingModel;
import com.android.urgetruck.UI.Models.RfidMappingResultModel;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleRfidMappingActivity extends AppCompatActivity implements RfidEventsListener {

    private Readers readers;
    private RFIDReader reader;
    private List<String> TagDataSet;
    private Button btnVehicleMapping;
    private TextInputLayout tv_rfid,textInputLayout_vehicleno;
    private TextInputEditText tvVrn;
    private AutoCompleteTextView autoCompleteTextView_rfid;
    private String RfidValue = "";
    private ProgressBar progressBar;
    View layout_toolbar;
    TextView toolbarText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagrfid);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        layout_toolbar = findViewById(R.id.layout_toolbar);
        initToolbar();
        btnVehicleMapping = findViewById(R.id.btnVehicleMapping);
        btnVehicleMapping.setText("Verify Tag");
        progressBar = findViewById(R.id.progressbar);
        tv_rfid = findViewById(R.id.tv_rfid);
        textInputLayout_vehicleno = findViewById(R.id.textInputLayout_vehicleno);
        textInputLayout_vehicleno.setVisibility(View.GONE);
        tvVrn = findViewById(R.id.tvVrn);
        autoCompleteTextView_rfid = findViewById(R.id.autoCompleteTextView_rfid);
        autoCompleteTextView_rfid.setText(RfidValue);
        TagDataSet = new ArrayList<>();
        connectReader();
        btnVehicleMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnVehicleMapping.getText().equals("Verify Tag")){
                    VerifyTag(view);
                }else if(btnVehicleMapping.getText().equals("Map")){
                    VerifyTagAndVrn();
                }
            }
        });
    }

    private void initToolbar() {
        toolbarText = layout_toolbar.findViewById(R.id.toolbarText);
        toolbarText.setText(getString(R.string.vehicle_rfidmapping_activity));
        ImageView ivLogo =layout_toolbar.findViewById(R.id.ivLogoLeftToolbar);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.ut_logo_with_outline);
        ivLogo.setOnClickListener(view -> {
            startActivity(new Intent(VehicleRfidMappingActivity.this,HomeActivity.class));
            finishAffinity();
        });
    }

    private void VerifyTagAndVrn() {
                String scanVRNInput = textInputLayout_vehicleno.getEditText().getText().toString().trim();
        if(scanVRNInput.isEmpty()){
            textInputLayout_vehicleno.setError("Please enter vehicle number");
        } else if(scanVRNInput.length()<8){
            textInputLayout_vehicleno.setError("Please enter 8 to 10 digits VRN");

        }else{
            callPostRfidMapApi(false);
        }
    }

    private void VerifyTag(View view) {
        String scanRFIDInput = tv_rfid.getEditText().getText().toString().trim();
        if(scanRFIDInput.isEmpty()){
            tv_rfid.setError("Press trigger to Scan RFID");
        } else{
            callPostRfidMapApi(false);
        }
        //callPostRfidMapApi();

    }

    private void callPostRfidMapApi(Boolean override) {

        if(Utils.isConnected(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);
            String baseurl= Utils.getSharedPreferences(VehicleRfidMappingActivity.this,"apiurl");

            ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);

            RfidMappingModel modal = null;
            String rfid = autoCompleteTextView_rfid.getText().toString().trim();
            autoCompleteTextView_rfid.setInputType(0);
            try {
                reader.disconnect();
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
            }

            if (btnVehicleMapping.getText().equals("Verify Tag")) {
                modal = new RfidMappingModel("123456", "",autoCompleteTextView_rfid.getText().toString().trim(), "False");


            } else if (btnVehicleMapping.getText().equals("Map") && !override) {

                modal = new RfidMappingModel("123456", tvVrn.getText().toString().trim(), autoCompleteTextView_rfid.getText().toString().trim(), "False");

            } else if(btnVehicleMapping.getText().equals("Map") && override){
                modal = new RfidMappingModel("123456", tvVrn.getText().toString().trim(), autoCompleteTextView_rfid.getText().toString().trim(), "True");

            }

            Log.e("Request",new Gson().toJson(modal));

            Call<RfidMappingResultModel> call = apiService.RfidMapping(modal);

            call.enqueue(new Callback<RfidMappingResultModel>() {
                @Override
                public void onResponse(Call<RfidMappingResultModel> call, Response<RfidMappingResultModel> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        if (btnVehicleMapping.getText().equals("Verify Tag")) {
                            textInputLayout_vehicleno.setVisibility(View.VISIBLE);
                            tvVrn.setText(response.body().getVrn());
                            tvVrn.setFocusable(false);
                            autoCompleteTextView_rfid.setText(rfid);
                            btnVehicleMapping.setVisibility(View.GONE);
                            Utils.showCustomDialog(VehicleRfidMappingActivity.this, response.body().getStatusMessage());


                        } else {
                            textInputLayout_vehicleno.setVisibility(View.VISIBLE);
                            tvVrn.setText(response.body().getVrn());
                            tvVrn.setFocusable(false);
                            autoCompleteTextView_rfid.setText(rfid);
                            btnVehicleMapping.setVisibility(View.GONE);
                            Utils.showCustomDialogFinish(VehicleRfidMappingActivity.this, response.body().getStatusMessage());


                        }

                    } else {

                        PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                        Log.e("msg", message.getStatusMessage());
                        if (message.getStatus().equals("RecordNotFound")) {
                          //  Utils.showCustomDialog(VehicleRfidMappingActivity.this, message.getStatusMessage());
                            textInputLayout_vehicleno.setVisibility(View.VISIBLE);
                            autoCompleteTextView_rfid.setText(rfid);
                            btnVehicleMapping.setText("Map");

                        } else if (message.getStatus().equals("Duplicate")) {
                          //  Utils.showCustomDialog(VehicleRfidMappingActivity.this, message.getStatusMessage());

                            showCustomDialog(VehicleRfidMappingActivity.this,"The vehicle already has a different RFID tag mapped. Do you want to overwrite?");
                            textInputLayout_vehicleno.setVisibility(View.VISIBLE);
                            autoCompleteTextView_rfid.setText(rfid);
                            tv_rfid.setFocusable(false);
                            tvVrn.setFocusable(false);
                            btnVehicleMapping.setVisibility(View.GONE);


                        }

                    }


                }

                @Override
                public void onFailure(Call<RfidMappingResultModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("error", t.toString());

                }
            });
        }else{
            Utils.showCustomDialog(this,getString(R.string.internet_connection));

        }

    }

    public void showCustomDialog(Context context, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callPostRfidMapApi(true);
                    }
                })
                .show();

    }

    @Override
    public void eventReadNotify(RfidReadEvents rfidReadEvents) {
        try {

            TagData detectedTag = rfidReadEvents.getReadEventData().tagData;

            if(detectedTag != null){
                reader.Actions.Inventory.stop();

                String tagID = detectedTag.getTagID();
                if(!TagDataSet.contains(tagID))
                    TagDataSet.add(tagID);
                ArrayAdapter<String> adapter1 =
                        new ArrayAdapter<>(
                                this,
                                R.layout.dropdown_menu_popup_item,
                                TagDataSet);

                AutoCompleteTextView editTextFilledExposedDropdown3 =
                        findViewById(R.id.autoCompleteTextView_rfid);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(TagDataSet.size()==1){
                            autoCompleteTextView_rfid.setText(adapter1.getItem(0).toString(), false);

                        }else{
                            autoCompleteTextView_rfid.setText("");
                            tv_rfid.setError("Select the RFID value from dropdown");
                        }
                        autoCompleteTextView_rfid.setAdapter(adapter1);



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
            if(rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT){

                if(rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  textView.setText("Scanning");
                            // Toast.makeText(MainActivity.this,"Looking for Tag",Toast.LENGTH_SHORT).show();
                        }
                    });

                    // start scanning

                    reader.Actions.Inventory.perform();
                }else if(rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED){
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

            if(!readerDevices.isEmpty()){
                reader = readerDevices.get(0).getRFIDReader();
                reader.connect();
                Antennas.AntennaRfConfig antennaRfConfig = reader.Config.Antennas.getAntennaRfConfig(1);
                antennaRfConfig.setrfModeTableIndex(0);
                antennaRfConfig.setTari(0);
                antennaRfConfig.setTransmitPowerIndex(Integer.parseInt(Utils.getSharedPreferences(VehicleRfidMappingActivity.this,"antennapower")));
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
        }catch (InvalidUsageException e){
            e.printStackTrace();
        }catch (OperationFailureException e){
            e.printStackTrace();
            Toast.makeText(this,"error connecting to scanner",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (reader != null) {
            try {
                reader.disconnect();
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
            }
        }
    }


}
