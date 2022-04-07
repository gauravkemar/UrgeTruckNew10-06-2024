package com.android.urgetruck.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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


public class ScanrfidFragment extends Fragment implements RfidEventsListener {

    private Readers readers;
    private RFIDReader reader;
    private List<String> TagDataSet;
    private RadioGroup rgVehicleDetails;
    private RadioButton rbScanRfid, rbVrn;
    private Boolean checkstate = true;
    private TextInputLayout tv_rfid, textInputLayout_vehicleno;
    private AutoCompleteTextView tvRfid;
    private TextInputEditText tvVrn;
    private AutoCompleteTextView autoCompleteTextView_rfid;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanrfid, container, false);
        Button scanFragment = view.findViewById(R.id.btn_scanrfid);
        rgVehicleDetails = view.findViewById(R.id.rgVehicleDetails);
        rbScanRfid = view.findViewById(R.id.rbScanRfid);
        tvRfid =
                view.findViewById(R.id.autoCompleteTextView_rfid);
        tv_rfid = view.findViewById(R.id.tv_rfid);
        autoCompleteTextView_rfid = view.findViewById(R.id.autoCompleteTextView_rfid);

        textInputLayout_vehicleno = view.findViewById(R.id.textInputLayout_vehicleno);
        rgVehicleDetails = view.findViewById(R.id.rgVehicleDetails);
        rbVrn = view.findViewById(R.id.rbVrn);
        tvVrn = view.findViewById(R.id.tvVrn);

        TagDataSet = new ArrayList<>();
        connectReader();

        scanFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput(view);

//
            }
        });
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


        return view;
    }

    @Override
    public void eventReadNotify(RfidReadEvents rfidReadEvents) {
        try {

            TagData detectedTag = rfidReadEvents.getReadEventData().tagData;

            if (detectedTag != null) {
                reader.Actions.Inventory.stop();

                String tagID = detectedTag.getTagID();
                if (!TagDataSet.contains(tagID))
                    TagDataSet.add(tagID);

                ArrayAdapter<String> adapter1 =
                        new ArrayAdapter<>(
                                getActivity(),
                                R.layout.dropdown_menu_popup_item,
                                TagDataSet);

                AutoCompleteTextView editTextFilledExposedDropdown3 =
                        getActivity().findViewById(R.id.autoCompleteTextView_rfid);


                getActivity().runOnUiThread(new Runnable() {
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
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {

                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {

                    getActivity().runOnUiThread(new Runnable() {
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

            readers = new Readers(getActivity(), ENUM_TRANSPORT.SERVICE_SERIAL);

            ArrayList<ReaderDevice> readerDevices = readers.GetAvailableRFIDReaderList();

            if (!readerDevices.isEmpty()) {
                reader = readerDevices.get(0).getRFIDReader();
                reader.connect();

                Antennas.AntennaRfConfig antennaRfConfig = reader.Config.Antennas.getAntennaRfConfig(1);
                antennaRfConfig.setrfModeTableIndex(0);
                antennaRfConfig.setTari(0);
                antennaRfConfig.setTransmitPowerIndex(Integer.parseInt(Utils.getSharedPreferences(getActivity(),"antennapower")));
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
            Toast.makeText(getActivity(), "error connecting to scanner", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    private boolean validateRFIDorVRN() {
        String scanRFIDInput = tv_rfid.getEditText().getText().toString().trim();
        String vrnInput = textInputLayout_vehicleno.getEditText().getText().toString().trim();
        if (rbScanRfid.isChecked() && scanRFIDInput.isEmpty()) {
            tv_rfid.setError("Press trigger to Scan RFID");
            return false;
        } else if (rbVrn.isChecked() && vrnInput.isEmpty()) {
            textInputLayout_vehicleno.setError("Please enter VRN");
            return false;
        } else if(rbVrn.isChecked() && vrnInput.length()<8) {
            textInputLayout_vehicleno.setError("Please enter 8 to 10 digits VRN");
            return false;

        }
        return true;


    }

    public void confirmInput(View v) {
        if (!validateRFIDorVRN()) {
            return;
        }
        if (!Utils.isConnected(getActivity())) {
            Utils.showCustomDialog(getActivity(), getString(R.string.internet_connection));
        } else {
            Bundle bundle = new Bundle();

            if (rgVehicleDetails.getCheckedRadioButtonId() == R.id.rbScanRfid) {

                bundle.putString("type","RFID");
                bundle.putString("typevalue",tvRfid.getText().toString().trim());


            } else if (rgVehicleDetails.getCheckedRadioButtonId() == R.id.rbVrn) {

                bundle.putString("type","VRN");
                bundle.putString("typevalue",tvVrn.getText().toString().trim());


            }

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            Log.e("message", "" + getActivity().getClass().getSimpleName());


            if (getActivity().getClass().getSimpleName().equals("SecurityInspectionActivity")) {

                      SecurityInspectionFragment securityInspectionFragment = new SecurityInspectionFragment();
                      securityInspectionFragment.setArguments(bundle);
                    transaction.replace(R.id.securityfragment_container, securityInspectionFragment);
                    transaction.commit();
                } else if (getActivity().getClass().getSimpleName().equals("ExitClearanceActivity")) {
                /*if(android.os.Build.MANUFACTURER.contains("Zebra Technologies") || android.os.Build.MANUFACTURER.contains("Motorola Solutions")) {*/
                    ExitClearanceListFragment exitClearanceListFragment = new ExitClearanceListFragment();
                    exitClearanceListFragment.setArguments(bundle);
                    transaction.replace(R.id.physicalcheckfragment_container,exitClearanceListFragment);
                    transaction.commit();
                /*}else{
                    Utils.showCustomDialogFinish(getActivity(),"Functionality not Available on non Zebra Devices");


                }*/}
                else if (getActivity().getClass().getSimpleName().equals("TrackVehicleActivity")) {
                    TrackVehicleFragment trackVehicleFragment = new TrackVehicleFragment();
                    trackVehicleFragment.setArguments(bundle);
                    transaction.replace(R.id.trackvehiclefragment_container,trackVehicleFragment);
                    transaction.commit();
                }


                }


        }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}