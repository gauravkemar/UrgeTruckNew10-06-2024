package com.android.urgetruck.UI.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.invoicecheckingstar.GetLoadingDetailOnVehicleDetailResponse;
import com.android.urgetruck.UI.Models.invoicecheckingstar.Product;
import com.android.urgetruck.UI.Models.invoicecheckingstar.ProductX;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneRequest;
import com.android.urgetruck.UI.Models.invoicecheckingstar.UpdateLoadingCompleteMilestoneResponse;
import com.android.urgetruck.UI.Utils.Utils;
import com.android.urgetruck.UI.adapter.InvoiceCheckingStarAdapter;
import com.android.urgetruck.UI.viemodel.InvoiceCheckingStarViewModel;
import com.android.urgetruck.databinding.ActivityInvoiceCheckingStarBinding;
import com.android.urgetruck.databinding.ScanLayoutBinding;
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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class InvoiceCheckingStarActivity extends AppCompatActivity implements RfidEventsListener {
    private Readers readers;
    private RFIDReader reader;
    private List<String> TagDataSet;
    private AutoCompleteTextView tvRfid;
    ArrayList<Product> productList;
    ArrayList<ProductX> productRequestList = new ArrayList<>();
    GetLoadingDetailOnVehicleDetailResponse getLoadingDetailOnVehicleDetailResponse;
    private RecyclerView rcInvoiceList;
    private InvoiceCheckingStarAdapter invoiceCheckingListAdapter;
    private TextInputLayout tv_rfid, textInputLayout_vehicleno;
    private RadioButton rbScanRfid, rbVrn;
    private RecyclerView rcExitClearanceInvoiceList;
    private InvoiceCheckingStarViewModel invoiceCheckingStarViewModel;
    ActivityInvoiceCheckingStarBinding binding;
    private MediaPlayer mediaPlayer;
    private RadioGroup rgVehicleDetails;
    String baseurl;
    private Boolean checkstate = true;
    private TextInputEditText tvVrn;
    ScanLayoutBinding scanLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invoice_checking_star);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        binding.toolbarText.setText("Loading");
        binding.ivLogoLeftToolbar.setVisibility(View.VISIBLE);
        binding.ivLogoLeftToolbar.setImageResource(R.drawable.ut_logo_with_outline);
        scanLayoutBinding = binding.scanLayout;
        invoiceCheckingStarViewModel = new ViewModelProvider(this).get(InvoiceCheckingStarViewModel.class);
        TagDataSet = new ArrayList<>();
        connectReader();
        tvRfid = scanLayoutBinding.autoCompleteTextViewRfid;
        tv_rfid =   scanLayoutBinding.tvRfid;
        mediaPlayer = MediaPlayer.create(this, R.raw.scanner_sound);
        baseurl = Utils.getSharedPreferences(InvoiceCheckingStarActivity.this, "apiurl");
        rgVehicleDetails = scanLayoutBinding.rgVehicleDetails;
        rbScanRfid = scanLayoutBinding.rbScanRfid;
        textInputLayout_vehicleno = scanLayoutBinding.textInputLayoutVehicleno;
        rcExitClearanceInvoiceList = binding.rcInvoiceList;
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
        binding.btnScanrfid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput(view);
            }
        });
        setContentView(binding.getRoot());
    }

    public void confirmInput(View v) {
        if (!validateRFIDorVRN()) {
            return;
        }
        if (!Utils.isConnected(this)) {
            Utils.showCustomDialog(this, getString(R.string.internet_connection));
        } else {

            if (rgVehicleDetails.getCheckedRadioButtonId() == R.id.rbScanRfid) {
                //bundle.putString("type","RFID");
                //bundle.putString("typevalue",tvRfid.getText().toString().trim());
                submitVin("RFID");
            } else if (rgVehicleDetails.getCheckedRadioButtonId() == R.id.rbVrn) {
                //bundle.putString("type","VRN");
                //bundle.putString("typevalue",tvVrn.getText().toString().trim());
                submitVin("VRN");
            }

        }
        //Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
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
                antennaRfConfig.setTransmitPowerIndex(Integer.parseInt(Utils.getSharedPreferences(InvoiceCheckingStarActivity.this, "antennapower")));
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

/*    private void submitVin() {
        try {
            String rfidScanned = tvRfid.getText().toString().trim();
            if (!rfidScanned.isEmpty()) {
                if (checkstate) {
                    getList(baseurl, generateRandom(), "", rfidScanned);
                } else {
                    getList(baseurl, generateRandom(), rfidScanned, "");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    private void submitVin(String type) {
        try {
            String scannedValue = "";
            if (type.equals("RFID")) {
                scannedValue = tvRfid.getText().toString().trim();
            } else if (type.equals("VRN")) {
                scannedValue = tvVrn.getText().toString().trim();
            }

            if (!scannedValue.isEmpty()) {
                if(checkstate)
                {
                    getList(baseurl, generateRandom(), "", scannedValue);
                }
                else {
                    getList(baseurl, generateRandom(), scannedValue, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getList(String baseUrl, int requestID, String vrn, String Rfid) {
        invoiceCheckingStarViewModel.getLoadingDetailOnVehicleResponse(baseUrl, requestID, vrn, Rfid).observe(this, new Observer<GetLoadingDetailOnVehicleDetailResponse>() {
            @Override
            public void onChanged(GetLoadingDetailOnVehicleDetailResponse resultResponse) {
                try {
                    if(resultResponse!=null)
                    {
                        getLoadingDetailOnVehicleDetailResponse = resultResponse;
                        if(resultResponse.getProduct().size() > 0 )
                        {
                            productList = (ArrayList<Product>) resultResponse.getProduct();
                            showOnRecyclerView(productList);
                            binding.clInvoiceList.setVisibility(View.VISIBLE);
                            binding.llScan.setVisibility(View.GONE);
                            binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    submitValidationList(resultResponse);
                                }
                            });
                        }
                    }
                    else {
                        binding.clInvoiceList.setVisibility(View.GONE);
                        binding.llScan.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Utils.showCustomDialog(InvoiceCheckingStarActivity.this, "Exception : No Data Found");
                }
            }
        });
    }

    private void submitList(String baseUrl, int requestID, String milestonetransactioncode, String milestonecode, String status, String vrn, ArrayList<ProductX> prodList) {
        UpdateLoadingCompleteMilestoneRequest updateLoadingCompleteMilestoneRequest = new
                UpdateLoadingCompleteMilestoneRequest(milestonecode
                , milestonetransactioncode,
                prodList, String.valueOf(requestID), status, vrn);
        invoiceCheckingStarViewModel.updateLoadingCompleteMilestone(baseUrl, updateLoadingCompleteMilestoneRequest).observe(this, new Observer<UpdateLoadingCompleteMilestoneResponse>() {
            @Override
            public void onChanged(UpdateLoadingCompleteMilestoneResponse resultResponse) {
                try {
                    if (resultResponse.getStatus().equals("Success")) {
                        Toast.makeText(InvoiceCheckingStarActivity.this, resultResponse.getStatus(), Toast.LENGTH_SHORT).show();
                        binding.clInvoiceList.setVisibility(View.GONE);
                        binding.llScan.setVisibility(View.VISIBLE);

                    } else if (resultResponse.getStatus().equals("Failed")) {
                        Toast.makeText(InvoiceCheckingStarActivity.this, resultResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        binding.clInvoiceList.setVisibility(View.GONE);
                        binding.llScan.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Utils.showCustomDialog(InvoiceCheckingStarActivity.this, "Exception : No Data Found");
                }


            }
        });
    }

    private void submitValidationList(GetLoadingDetailOnVehicleDetailResponse response) {
        try {
            if (productRequestList.size() > 0) {
                submitList(baseurl, generateRandom(), response.getMilestoneTransactionCode(),
                        response.getMilestoneCode(), "Success", response.getVrn(), productRequestList);
            } else {
                Toast.makeText(InvoiceCheckingStarActivity.this, "Please Check the products!!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }

    public int generateRandom() {
        // Create an instance of the Random class
        Random random = new Random();

        // Define the range
        int lowerBound = 111111;
        int upperBound = 999999;

        // Generate a random number within the specified range
        int randomNumber = random.nextInt(upperBound - lowerBound + 1) + lowerBound;

        // Print the generated random number
        System.out.println("Random Number: " + randomNumber);
        return randomNumber;
    }

    private void showOnRecyclerView(ArrayList<Product> productList) {
        rcInvoiceList = binding.rcInvoiceList;
        invoiceCheckingListAdapter = new InvoiceCheckingStarAdapter(this, productList);
        rcInvoiceList.setAdapter(invoiceCheckingListAdapter);
        invoiceCheckingListAdapter.setOnItemClickListener(new InvoiceCheckingStarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductX product) {
                productRequestList.add(product);
            }

            @Override
            public void onItemUnchecked(ProductX product) {
                /*for (ProductX item : productRequestList) {
                   if(item.equals(product.getProducttransactioncode()))
                   {
                       productRequestList.remove()
                   }
                }*/
                Iterator<ProductX> iterator = productRequestList.iterator();
                while (iterator.hasNext()) {
                    ProductX item = iterator.next();
                    if (item.getProducttransactioncode().equals(product.getProducttransactioncode())) {
                        iterator.remove();
                    }
                }
            }
        });
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

    @Override
    public void eventReadNotify(RfidReadEvents rfidReadEvents) {
        try {
            TagData detectedTag = rfidReadEvents.getReadEventData().tagData;
            mediaPlayer.start();
            if (detectedTag != null) {
                if(binding.llScan.getVisibility()==View.VISIBLE)
                {
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
                }
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
    @Override
    public void onResume() {
        super.onResume();
        if (reader == null) {
            connectReader();
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