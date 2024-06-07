package com.android.urgetruck.UI.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.exitclearancenew.ExitClearanceInvoicingResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.ExitClearanceProductVerificationResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.ExitInvoiceUpdateListResponse;
import com.android.urgetruck.UI.Models.exitclearancenew.InvoiceDetail;
import com.android.urgetruck.UI.Models.exitclearancenew.ProductDetail;
import com.android.urgetruck.UI.Utils.Utils;
import com.android.urgetruck.UI.adapter.ExitClearanceNewAdapter;
import com.android.urgetruck.UI.adapter.ExitClearanceProductVerificationAdapter;
import com.android.urgetruck.UI.viemodel.InvoiceCheckingStarViewModel;
import com.android.urgetruck.databinding.ActivityExitClearanceNewBinding;
import com.android.urgetruck.databinding.ScanLayoutBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ExitClearanceNewActivity extends AppCompatActivity implements RfidEventsListener, EMDKManager.EMDKListener
, Scanner.StatusListener, Scanner.DataListener{
    private Readers readers;
    private RFIDReader reader;
    private List<String> TagDataSet;
    private AutoCompleteTextView tvRfid;
    private TextInputLayout tv_rfid, textInputLayout_vehicleno;
    private RadioButton rbScanRfid, rbVrn;

    private MediaPlayer mediaPlayer;
    private RadioGroup rgVehicleDetails;
    private RecyclerView rcExitClearanceInvoiceList;
    String baseurl;
    private Boolean checkstate = true;
    private TextInputEditText tvVrn;
    private InvoiceCheckingStarViewModel invoiceCheckingStarViewModel;
    ActivityExitClearanceNewBinding binding;
    private ExitClearanceNewAdapter exitClearanceNewAdapter;
    ExitClearanceInvoicingResponse exitClearanceInvoicingResponse;
    ArrayList<InvoiceDetail> invoiceDetailList = new ArrayList<>();
    //ArrayList<InvoiceDetail> submitInvoiceCheckedList = new ArrayList<>();
    HashMap<String,Boolean> invoiceHashMapList = new HashMap<String,Boolean>();
    FrameLayout exitClearanceContainer;
    ScanLayoutBinding scanLayoutBinding;
    Bundle bundle = new Bundle();

    private static final String TAG = "ParkReparkActivity";
    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private boolean isBarcodeInit = false;
    private boolean resumeFlag = false;
    Scanner scanner = null;
    String singleScannedInvoice="";

    //exitclearance product verification
    private ExitClearanceProductVerificationAdapter exitClearanceProductAdapter;
    ExitClearanceProductVerificationResponse exitClearanceProductVerificationResponse;
    private RecyclerView rcExitClearanceProductVerification;
    ArrayList<ProductDetail> productDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_exit_clearance_new);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        binding = DataBindingUtil.setContentView(this,R.layout.activity_exit_clearance_new);
        binding.toolbarText.setText("Exit Clearance");
        binding.ivLogoLeftToolbar.setVisibility(View.VISIBLE);
        binding.ivLogoLeftToolbar.setImageResource(R.drawable.ut_logo_with_outline);
        scanLayoutBinding = binding.scanLayout;
        invoiceCheckingStarViewModel = new ViewModelProvider(this).get(InvoiceCheckingStarViewModel.class);
        TagDataSet = new ArrayList<>();
        connectReader();
        tvRfid = scanLayoutBinding.autoCompleteTextViewRfid;
        tv_rfid =   scanLayoutBinding.tvRfid;
        mediaPlayer = MediaPlayer.create(this, R.raw.scanner_sound);
        baseurl = Utils.getSharedPreferences(ExitClearanceNewActivity.this, "apiurl");
        rgVehicleDetails = scanLayoutBinding.rgVehicleDetails;
        rbScanRfid = scanLayoutBinding.rbScanRfid;
        textInputLayout_vehicleno = scanLayoutBinding.textInputLayoutVehicleno;
        rcExitClearanceInvoiceList = binding.rcInvoiceList;
        exitClearanceContainer = binding.exitClearanceContainer;





        rbVrn = scanLayoutBinding.rbVrn;
        tvVrn = scanLayoutBinding.tvVrn;
        initBarcode();
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
        if (!Utils.isConnected(ExitClearanceNewActivity.this)) {
            Utils.showCustomDialog(ExitClearanceNewActivity.this, getString(R.string.internet_connection));
        } else {

            if (rgVehicleDetails.getCheckedRadioButtonId() == R.id.rbScanRfid) {
                bundle.putString("type","RFID");
                bundle.putString("typevalue",tvRfid.getText().toString().trim());
                submitVin("RFID");
            } else if (rgVehicleDetails.getCheckedRadioButtonId() == R.id.rbVrn) {
                bundle.putString("type","VRN");
                bundle.putString("typevalue",tvVrn.getText().toString().trim());
                submitVin("VRN");
            }

        }
        //Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }


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
                    getExitClearanceInvoiceList(baseurl, generateRandom(), "", scannedValue);
                }
                else {
                    getExitClearanceInvoiceList(baseurl, generateRandom(), scannedValue, "");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  /*  private void submitVin(String type) {
        try {
            String rfidScanned = tvRfid.getText().toString().trim();
            String vrnScanned = tvVrn.getText().toString().trim();
            if(type.equals("RFID"))
            {
                if (!rfidScanned.isEmpty()) {
                    if (checkstate) {
                        getExitClearanceInvoiceList(baseurl, generateRandom(), "", rfidScanned);
                    } else {
                        getExitClearanceInvoiceList(baseurl, generateRandom(), rfidScanned, "");
                    }

                }
            }
            else if(type.equals("VRN"))
            {
                if (!vrnScanned.isEmpty()) {
                    if (checkstate) {
                        getExitClearanceInvoiceList(baseurl, generateRandom(), "", vrnScanned);
                    } else {
                        getExitClearanceInvoiceList(baseurl, generateRandom(), vrnScanned, "");
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/
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
                antennaRfConfig.setTransmitPowerIndex(Integer.parseInt(Utils.getSharedPreferences(ExitClearanceNewActivity.this, "antennapower")));
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

    ////exit clearance check
    private void getExitClearanceInvoiceList(String baseUrl, int requestID, String vrn, String Rfid) {
        binding.progressbar.setVisibility(View.VISIBLE);
        invoiceCheckingStarViewModel.getExitClearanceInvoiceMutableLiveData(ExitClearanceNewActivity.this,baseUrl, requestID, vrn, Rfid).observe(this, new Observer<ExitClearanceInvoicingResponse>() {
            @Override
            public void onChanged(ExitClearanceInvoicingResponse resultResponse) {
                binding.progressbar.setVisibility(View.GONE);
                try {
                    if(resultResponse!=null)
                    {
                        binding.clInvoiceList.setVisibility(View.VISIBLE);
                        binding.llScan.setVisibility(View.GONE);
                        exitClearanceInvoicingResponse = resultResponse;
                        int getVehicleTransactionId = resultResponse.getVehicelTransactionId();
                        String getVRNValue=resultResponse.getVrn();
                        binding.tvVRNValue.setText(getVRNValue);
                        binding.tvTransactionIdValue.setText(String.valueOf(getVehicleTransactionId));
                        if(resultResponse.getInvoiceDetail().size() > 0 )
                        {
                            binding.tableFirstItemInvoiceList.getRoot().setVisibility(View.VISIBLE);
                            binding.rcInvoiceList.setVisibility(View.VISIBLE);
                            invoiceDetailList = (ArrayList<InvoiceDetail>) resultResponse.getInvoiceDetail();
                            showOnRecyclerView();
                            binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    submitExitClearanceList(resultResponse);
                                }
                            });

                        }
                        else {
                            binding.tableFirstItemInvoiceList.getRoot().setVisibility(View.GONE);
                            binding.rcInvoiceList.setVisibility(View.GONE);
                            binding.tvNoInvoiceFound.setVisibility(View.VISIBLE);
                            binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    submitExitClearanceList(resultResponse);
                                }
                            });

                        }
                    }
                    else
                    {
                        binding.clInvoiceList.setVisibility(View.GONE);
                        binding.llScan.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Utils.showCustomDialog(ExitClearanceNewActivity.this, "Exception : No Data Found");
                }
            }
        }
        );
    }
    private void submitExitClearanceList(ExitClearanceInvoicingResponse response) {
        try {
            if (isAllVerified(invoiceDetailList)) {
                List<String> invoiceNumbersFromResponse = getInvoiceNumbersFromResponse(response);
                List<String> checkedList = new ArrayList<>();
                for(InvoiceDetail detail:invoiceDetailList)
                {
                    checkedList.add(detail.getInvoiceNumber());
                }
                if (checkedList.containsAll(invoiceNumbersFromResponse)) {
                    submitList(baseurl, response);
                } else {
                    Toast.makeText(ExitClearanceNewActivity.this, "Please verify the pending products!!", Toast.LENGTH_SHORT).show();
                }

            } else if(invoiceDetailList.size() == 0 ){

                submitList(baseurl, response);
            }
            else {
                Toast.makeText(ExitClearanceNewActivity.this, "Please verify the pending products!!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }
    private List<String> getInvoiceNumbersFromResponse(ExitClearanceInvoicingResponse response) {
        List<String> invoiceNumbers = new ArrayList<>();
        List<InvoiceDetail> invoiceDetails = response.getInvoiceDetail();
        for (InvoiceDetail detail : invoiceDetails) {
            invoiceNumbers.add(detail.getInvoiceNumber());
        }
        return invoiceNumbers;
    }
    private void submitList(String baseUrl,  ExitClearanceInvoicingResponse response) {
        binding.progressbar.setVisibility(View.VISIBLE);
        ExitClearanceInvoicingResponse updateLoadingCompleteMilestoneRequest = new
                ExitClearanceInvoicingResponse(response.getDriverCode(),
                response.getDriverName(), response.getErrorMessage(),invoiceDetailList,response.getJobMilestoneId(),
                response.getMilestoneCode(),response.getMilestoneStatus(),response.getMilestoneStatus(),response.getStatus(),response.getVehicelTransactionId(),response.getVrn());
        invoiceCheckingStarViewModel.updateExitClearanceInvoiceMutableLiveData (ExitClearanceNewActivity.this,baseUrl, updateLoadingCompleteMilestoneRequest).observe(this, new Observer<ExitInvoiceUpdateListResponse>() {
            @Override
            public void onChanged(ExitInvoiceUpdateListResponse resultResponse) {
                binding.progressbar.setVisibility(View.GONE);
                try {
                    if(resultResponse!=null)
                    {
                        if(resultResponse.getStatusCode()==200)
                        {
                            //Toast.makeText(ExitClearanceNewActivity.this,"Submited Succesfuly!",Toast.LENGTH_SHORT).show();

                            if(resultResponse.getNextPage())
                            {
                                getExitClearanceProductList(baseurl,generateRandom(),response.getVrn());
                            }
                            else {
                                Utils.showCustomDialogFinish(ExitClearanceNewActivity.this,"Submited Succesfuly!" );
                            }

                            //finish();
                        }
                        else if(resultResponse.getStatusCode()==400 || resultResponse.getStatusCode()==404){
                            //Toast.makeText(ExitClearanceNewActivity.this,resultResponse.getErrorMessage(),Toast.LENGTH_SHORT).show();
                            Utils.showCustomDialogFinish(ExitClearanceNewActivity.this,resultResponse.getErrorMessage() );
                            //finish();
                        }
                    }
                } catch (Exception e) {
                    Utils.showCustomDialog(ExitClearanceNewActivity.this, "Exception : No Data Found");
                }


         /*       FragmentManager fragmentManager = getSupportFragmentManager(); // or getFragmentManager() if not using AndroidX
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                ExitClearanceListFragment exitClearanceListFragment = new ExitClearanceListFragment();
                exitClearanceListFragment.setArguments(bundle);
                transaction.replace(R.id.physicalcheckfragment_container, exitClearanceListFragment);
                transaction.addToBackStack(null); // Add this line if you want to add the transaction to the back stack
                transaction.commit();
*/

             /*   if (resultResponse.getStatus().equals("Success")) {
                    Toast.makeText(ExitClearanceNewActivity.this, resultResponse.getStatus(), Toast.LENGTH_SHORT).show();
                    binding.clInvoiceList.setVisibility(View.GONE);
                    binding.llScan.setVisibility(View.VISIBLE);

                } else if (resultResponse.getStatus().equals("Failed")) {
                    Toast.makeText(ExitClearanceNewActivity.this, resultResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    binding.clInvoiceList.setVisibility(View.GONE);
                    binding.llScan.setVisibility(View.VISIBLE);
                }*/
            }
        });
    }
    private boolean isAllVerified(List<InvoiceDetail> invoiceDetailsList) {
        for (InvoiceDetail detail : invoiceDetailsList) {
            if (!detail.isVerified()) {
                return false; // If any item is not verified, return false
            }
        }
        return true; // All items are verified
    }
    private void showOnRecyclerView() {
        rcExitClearanceInvoiceList = binding.rcInvoiceList;
        exitClearanceNewAdapter = new ExitClearanceNewAdapter(this, invoiceDetailList,rcExitClearanceInvoiceList);
        rcExitClearanceInvoiceList.setAdapter(exitClearanceNewAdapter);
        rcExitClearanceInvoiceList.setLayoutManager(new LinearLayoutManager(this));
        exitClearanceNewAdapter.setOnItemClickListener( new   ExitClearanceNewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position,InvoiceDetail product) {
                //submitInvoiceCheckedList.add(product);
                //Log.d("thisChecked","checked "+submitInvoiceCheckedList.toString());
              /*  ArrayList<String> submitListProd=new ArrayList();
                for(InvoiceDetail invoiceDetail:submitInvoiceCheckedList)
                {
                    submitListProd.add(invoiceDetail.getInvoiceNumber());
                }
                if (!submitListProd.contains(product.getInvoiceNumber())) {
                    submitInvoiceCheckedList.add(product);
                    // Log.d("thisChecked", "checked " + submitInvoiceCheckedList.toString());
                } else {
                    // Item already exists, handle accordingly (e.g., show a message).
                    // You can also remove the item if you want to toggle the selection.
                    // submitInvoiceCheckedList.remove(product);
                    Toast.makeText(ExitClearanceNewActivity.this,"Already Scanned!!",Toast.LENGTH_SHORT).show();
                }

*/
            }

            @Override
            public void onItemUnchecked(InvoiceDetail product) {
             /*   for (ProductX item : productRequestList) {
                   if(item.equals(product.getProducttransactioncode()))
                   {
                       productRequestList.remove()
                   }
                }
                //Log.d("thisChecked","unchecked"+submitInvoiceCheckedList.toString() );
                Iterator<InvoiceDetail> iterator = submitInvoiceCheckedList.iterator();
                while (iterator.hasNext()) {
                    InvoiceDetail item = iterator.next();
                    if (item.getInvoiceNumber().equals(product.getInvoiceNumber())) {
                        iterator.remove();
                    }
                }*/
            }

            @Override
            public void onVerifyBarcodeClick(String invoiceNo, int position) {
                if (android.os.Build.MANUFACTURER.contains("Zebra Technologies") || android.os.Build.MANUFACTURER.contains("Motorola Solutions")) {
                    scanBarcode();
                    singleScannedInvoice=invoiceNo;
                }
            }
        });
    }
    public void scanBarcode() {
        if (scanner == null || scanner != null) {
            // Get default scanner defined on the device
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
            if (scanner != null) {
                // Implement the DataListener interface and pass the pointer of this object to get the data callbacks.
                scanner.addDataListener(this);

                // Implement the StatusListener interface and pass the pointer of this object to get the status callbacks.
                scanner.addStatusListener(this);
//                ScannerConfig config = null;
//                try {
//                    config = scanner.getConfig();
//                    config.readerParams.readerSpecific.imagerSpecific.scanMode = ScannerConfig.ScanMode.MULTI_BARCODE;
//                    // If it is a camera
//                    config.readerParams.readerSpecific.cameraSpecific.scanMode = ScannerConfig.ScanMode.MULTI_BARCODE;
//
//                    // Setting the barcode count
//
//
//                    scanner.setConfig(config);
//                } catch (ScannerException e) {
//                    e.printStackTrace();
//                }

                // Scan Mode set to Multi Barcode
                // If it is a imager


                // Hard trigger. When this mode is set, the user has to manually
                // press the trigger on the device after issuing the read call.
                // NOTE: For devices without a hard trigger, use TriggerType.SOFT_ALWAYS.
                scanner.triggerType = Scanner.TriggerType.SOFT_ONCE;

                try {
                    // Enable the scanner
                    // NOTE: After calling enable(), wait for IDLE status before calling other scanner APIs
                    // such as setConfig() or read().
                    scanner.enable();
                    scanner.read();

                } catch (ScannerException e) {
                    updateStatus(e.getMessage());
                    deInitScanner();
                }
            } else {
                updateStatus("Failed to   initialize the scanner device.");
            }
        }
    }



    //////physical check
    private void updateStatus(final String status) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // statusTextView.setText(""+  status);
                Log.d(String.valueOf(this),"Status "+ status);

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (isBarcodeInit) {
            deInitScanner();
        }
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
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

    @Override
    protected void onPause() {
        super.onPause();
        if (isBarcodeInit) {
            deInitScanner();
        }
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
    private void initBarcode() {
        isBarcodeInit = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        EMDKResults results = EMDKManager.getEMDKManager(this, this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Log.e(TAG, "EMDKManager object request failed!");
        } else {
            Log.e(TAG, "EMDKManager object initialization is in progress.......");
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        if (Build.MANUFACTURER.contains("Zebra Technologies") || Build.MANUFACTURER.contains("Motorola Solutions")) {
            this.emdkManager = emdkManager;
            initBarcodeManager();
            initScanner();
        }
    }
    @Override
    public void onClosed() {
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resumeFlag) {
            resumeFlag = false;
            if (Build.MANUFACTURER.contains("Zebra Technologies") || Build.MANUFACTURER.contains("Motorola Solutions")) {
                initBarcodeManager();
                initScanner();
            }
        }
        if (reader == null) {
            connectReader();
        }
    }
    private void initBarcodeManager() {
        barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);

        if (barcodeManager == null) {
            Toast.makeText(
                    ExitClearanceNewActivity.this,
                    "Barcode scanning is not supported.",
                    Toast.LENGTH_LONG
            ).show();
            finish();
        }
    }
    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        String dataStr = "";

        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            mediaPlayer.start();
            ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();
            // Iterate through scanned data and prepare the data.
            for (ScanDataCollection.ScanData data : scanData) {
                // Get the scanned data
                String barcodeData = data.getData();
                // Get the type of label being scanned
                ScanDataCollection.LabelType labelType = data.getLabelType();
                // Concatenate barcode data and label type
                dataStr = barcodeData;
            }
            String finalDataStr = dataStr;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //exitClearanceNewAdapter.markCheckboxByTag(finalDataStr);
             /*       for (int i = 0; i < invoiceDetailList.size(); i++) {
                        if (invoiceDetailList.get(i).getInvoiceNumber().equals(finalDataStr)) {
                            invoiceDetailList.get(i).updateFields(false, true, "Pending");
                            exitClearanceNewAdapter.notifyItemChanged(i);
                            break;
                        }
                    }*/
                    defaultBarcodeScanned(finalDataStr);

                   // exitClearanceNewAdapter.notifyItemChanged(invoiceDetailList.size()-1);
               }
            });
        }


   /*     runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String dataStr = "";
                if (scanDataCollection != null && scanDataCollection.getResult() == ScannerResults.SUCCESS) {
                    mediaPlayer.start();
                    List<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();
                    for (ScanDataCollection.ScanData data : scanData) {
                        String barcodeData = data.getData();
                        dataStr = barcodeData;
                    }
                    exitClearanceNewAdapter.markCheckboxByTag(dataStr);
                    exitClearanceNewAdapter.notifyItemChanged(invoiceDetailList.size()-1);
                    //exitClearanceNewAdapter.notifyDataSetChanged();

       *//*     runOnUiThread(() -> binding.tvBarcode.setText(dataStr));
            // checkVehicleInsideGeofenceBarcode(dataStr.toString());
            if (dataStr != null) {
                getVehicleStatusBarcode(dataStr);
            }*//*
                    Log.e(TAG, "Barcode Data : " + dataStr);
                }
            }
        });*/
    }

    private void defaultBarcodeScanned(String finalDataStr)
    {

        if(binding.clInvoiceList.getVisibility()==View.VISIBLE)
        {
            if (!singleScannedInvoice.isEmpty()) {
                verifyInvoice(singleScannedInvoice,finalDataStr);
                singleScannedInvoice = ""; // Clear singleScannedInvoice after verifying
            } else {
                for (int i = 0; i < invoiceDetailList.size(); i++) {
                    if (invoiceDetailList.get(i).getInvoiceNumber().equals(finalDataStr)) {
                        if (!invoiceDetailList.get(i).isVerified()) { // Check if the item is not already verified
                            invoiceDetailList.get(i).updateFields(false, true, "Pending");
                            exitClearanceNewAdapter.notifyItemChanged(i);
                        } else {
                            // Handle the case where the item is already verified
                            // You can show a toast message or perform any other action here
                            Toast.makeText(ExitClearanceNewActivity.this, "This item is already verified!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }

        }
        else if( binding.clProductVerification.getVisibility()==View.VISIBLE){
            if (!singleScannedInvoice.isEmpty()) {
                verifyBatchNo(singleScannedInvoice,finalDataStr);
                singleScannedInvoice = ""; // Clear singleScannedInvoice after verifying
            } else {
                for (int i = 0; i < productDetailList.size(); i++) {
                    if (productDetailList.get(i).getBatchNumber().equals(finalDataStr.replaceFirst("^0+", ""))) {
                        if (!productDetailList.get(i).isVerified) { // Check if the item is not already verified
                            productDetailList.get(i).updateFields( true);
                            exitClearanceProductAdapter.notifyItemChanged(i);
                        } else {
                            // Handle the case where the item is already verified
                            // You can show a toast message or perform any other action here
                            Toast.makeText(ExitClearanceNewActivity.this, "This item is already verified!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }
        }




        /*singleScannedInvoice
        for (int i = 0; i < invoiceDetailList.size(); i++) {
            if (invoiceDetailList.get(i).getInvoiceNumber().equals(finalDataStr)) {
                if (!invoiceDetailList.get(i).isVerified()) { // Check if the item is not already verified
                    invoiceDetailList.get(i).updateFields(false, true, "Pending");
                    exitClearanceNewAdapter.notifyItemChanged(i);
                } else {
                    // Handle the case where the item is already verified
                    // You can show a toast message or perform any other action here
                    Toast.makeText(ExitClearanceNewActivity.this, "This item is already verified!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }*/
    }
    private void verifyInvoice(String invoiceNumber, String finalDataStr) {
        if (invoiceNumber.toLowerCase().equals(finalDataStr.toLowerCase())) {
            for (int i = 0; i < invoiceDetailList.size(); i++) {
                if (invoiceDetailList.get(i).getInvoiceNumber().equals(invoiceNumber)) {
                    if (!invoiceDetailList.get(i).isVerified()) { // Check if the item is not already verified
                        invoiceDetailList.get(i).updateFields(false, true, "Pending");
                        exitClearanceNewAdapter.notifyItemChanged(i);
                    } else {
                        // Handle the case where the item is already verified
                        // You can show a toast message or perform any other action here
                        Toast.makeText(ExitClearanceNewActivity.this, "This item is already verified!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        } else {
            // Notify user that invoiceNumber does not match finalDataStr
            Toast.makeText(ExitClearanceNewActivity.this, "Scanned invoice does not match!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onStatus(StatusData statusData) {
        StatusData.ScannerStates state = statusData.getState();
        String statusStr = "";
        switch (state) {
            case IDLE:
                statusStr = statusData.getFriendlyName() + " is enabled and idle...";
                //etConfig();
                try {
                    scanner.read();
                } catch (ScannerException e) {
                    e.printStackTrace();
                }
                break;
            case WAITING:
                statusStr = "Scanner is waiting for trigger press...";
                break;
            case SCANNING:
                statusStr = "Scanning...";
                break;
            case DISABLED:
                // Do nothing for DISABLED state
                break;
            case ERROR:
                statusStr = "An error has occurred.";
                break;
            default:
                // Do nothing for other states
                break;
        }
        setStatusText(statusStr);
    }
/*    private void setConfig() {
        if (scanner != null) {
            try {

                Scanner.Config config = scanner.getConfig();
                if (config.isParamSupported("config.scanParams.decodeHapticFeedback")) {
                    config.scanParams.decodeHapticFeedback = true;
                }
                scanner.setConfig(config);
            } catch (ScannerException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }*/

    private void setStatusText(String msg) {
        Log.e(TAG, "StatusText: " + msg);
    }
    void initScanner() {
        if (scanner == null) {
            barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);

            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                scanner.triggerType = Scanner.TriggerType.HARD;
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void deInitScanner() {
        if (scanner != null) {
            try {
                scanner.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            scanner = null;
        }
    }


    ////product verification
    ////exit clearance check
    private void getExitClearanceProductList(String baseUrl, int requestID, String vrn) {
        binding.progressbar.setVisibility(View.VISIBLE);
        invoiceCheckingStarViewModel.getExitClearanceProductVerification(ExitClearanceNewActivity.this,baseUrl, requestID, vrn,"").observe(this, new Observer<ExitClearanceProductVerificationResponse>() {
            @Override
                    public void onChanged(ExitClearanceProductVerificationResponse resultResponse) {
                        binding.progressbar.setVisibility(View.GONE);
                        try {
                            if(resultResponse!=null)
                            {
                                binding.clInvoiceList.setVisibility(View.GONE);
                                binding.rcInvoiceList.setVisibility(View.GONE);
                                binding.clProductVerification.setVisibility(View.VISIBLE);
                                exitClearanceProductVerificationResponse = resultResponse;
                                int getVehicleTransactionId = resultResponse.getVehicelTransactionId();
                                String getVRNValue=resultResponse.getVrn();
                                binding.tvProdVRNValue.setText(getVRNValue);
                                binding.tvProdTransactionIdValue.setText(String.valueOf(getVehicleTransactionId));
                                if(resultResponse.getProductDetail().size() > 0 )
                                {
                                    binding.tableFirstItemProductList.getRoot().setVisibility(View.VISIBLE);
                                    productDetailList = (ArrayList<ProductDetail>) resultResponse.getProductDetail();
                                    showOnProductListRecyclerView();
                                    binding.btnProdSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            submitExitClearanceProductList(resultResponse);
                                        }
                                    });

                                }
                                else {
                                    binding.tableFirstItemInvoiceList.getRoot().setVisibility(View.GONE);
                                    binding.rcInvoiceList.setVisibility(View.GONE);
                                    binding.tvNoInvoiceFound.setVisibility(View.VISIBLE);
                                    binding.btnProdSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            submitExitClearanceProductList(resultResponse);
                                        }
                                    });

                                }
                            }
                            else
                            {
                                binding.clInvoiceList.setVisibility(View.GONE);
                                binding.llScan.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Utils.showCustomDialog(ExitClearanceNewActivity.this, "Exception : No Data Found");
                        }
                    }
                }
        );
    }
    private void submitExitClearanceProductList(ExitClearanceProductVerificationResponse response) {
        try {
            if (isAllProductVerified(productDetailList)) {
                List<String> invoiceNumbersFromResponse = getInvoiceNumbersFromResponse(response);
                List<String> checkedList = new ArrayList<>();
                for(ProductDetail detail:productDetailList)
                {
                    checkedList.add(detail.getBatchNumber());
                }
                if (checkedList.containsAll(invoiceNumbersFromResponse)) {
                    submitProductList(baseurl, response);
                } else {
                    Toast.makeText(ExitClearanceNewActivity.this, "Please verify the pending products!!", Toast.LENGTH_SHORT).show();
                }

            } else if(invoiceDetailList.size() == 0 ){

                submitProductList(baseurl, response);
            }
            else {
                Toast.makeText(ExitClearanceNewActivity.this, "Please verify the pending products!!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }
    private List<String> getInvoiceNumbersFromResponse(ExitClearanceProductVerificationResponse response) {
        List<String> batchNo = new ArrayList<>();
        List<ProductDetail> invoiceDetails = response.getProductDetail();
        for (ProductDetail detail : invoiceDetails) {
            batchNo.add(detail.getBatchNumber());
        }
        return batchNo;
    }
    private void submitProductList(String baseUrl,  ExitClearanceProductVerificationResponse response) {
        binding.progressbar.setVisibility(View.VISIBLE);
        ExitClearanceProductVerificationResponse updateLoadingCompleteMilestoneRequest = new
                ExitClearanceProductVerificationResponse(response.getDriverCode(),
                response.getDriverName(), response.getErrorMessage(),productDetailList,response.getJobMilestoneId(),
                response.getMilestoneCode(),response.getMilestoneStatus(),response.getMilestoneStatus(),response.getStatus(),response.getVehicelTransactionId(),response.getVrn());
        invoiceCheckingStarViewModel.updateProductDetailOnVehicleDetailMutableLiveData (
                ExitClearanceNewActivity.this,baseUrl,
                updateLoadingCompleteMilestoneRequest).observe(
                        this, new Observer<ExitInvoiceUpdateListResponse>() {
            @Override
            public void onChanged(ExitInvoiceUpdateListResponse resultResponse) {
                binding.progressbar.setVisibility(View.GONE);
                try {
                    if(resultResponse!=null)
                    {
                        if(resultResponse.getStatusCode()==200)
                        {
                            //Toast.makeText(ExitClearanceNewActivity.this,"Submited Succesfuly!",Toast.LENGTH_SHORT).show();

                            if(resultResponse.getNextPage())
                            {

                            }
                            else {
                                Utils.showCustomDialogFinish(ExitClearanceNewActivity.this,"Submited Succesfuly!" );
                            }

                            //finish();
                        }
                        else if(resultResponse.getStatusCode()==400 || resultResponse.getStatusCode()==404){
                            //Toast.makeText(ExitClearanceNewActivity.this,resultResponse.getErrorMessage(),Toast.LENGTH_SHORT).show();
                            Utils.showCustomDialogFinish(ExitClearanceNewActivity.this,resultResponse.getErrorMessage() );
                            //finish();
                        }
                    }
                } catch (Exception e) {
                    Utils.showCustomDialog(ExitClearanceNewActivity.this, "Exception : No Data Found");
                }
            }
        });
    }
    private boolean isAllProductVerified(List<ProductDetail> invoiceDetailsList) {
        for (ProductDetail detail : invoiceDetailsList) {
            if (!detail.isVerified) {
                return false; // If any item is not verified, return false
            }
        }
        return true; // All items are verified
    }

    private void verifyBatchNo(String batchNo, String finalDataStr) {
        if (batchNo.toLowerCase().equals(finalDataStr.replaceFirst("^0+", "").toLowerCase())) {
            for (int i = 0; i < productDetailList.size(); i++) {
                if (productDetailList.get(i).getBatchNumber().equals(batchNo)) {
                    if (!productDetailList.get(i).isVerified) { // Check if the item is not already verified
                        productDetailList.get(i).updateFields(true);
                        exitClearanceProductAdapter.notifyItemChanged(i);
                    } else {
                        // Handle the case where the item is already verified
                        // You can show a toast message or perform any other action here
                        Toast.makeText(ExitClearanceNewActivity.this, "This item is already verified!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        } else {
            // Notify user that invoiceNumber does not match finalDataStr
            Toast.makeText(ExitClearanceNewActivity.this, "Scanned invoice does not match!", Toast.LENGTH_SHORT).show();
        }
    }
    private void showOnProductListRecyclerView() {
        rcExitClearanceProductVerification = binding.rcProdList;
        exitClearanceProductAdapter = new ExitClearanceProductVerificationAdapter(this, productDetailList,rcExitClearanceProductVerification);
        rcExitClearanceProductVerification.setAdapter(exitClearanceProductAdapter);
        rcExitClearanceProductVerification.setLayoutManager(new LinearLayoutManager(this));
        exitClearanceProductAdapter.setOnItemClickListener( new   ExitClearanceProductVerificationAdapter.OnItemClickListener() {
            @Override
            public void onVerifyBarcodeClick(String batchNo, int position) {
                if (android.os.Build.MANUFACTURER.contains("Zebra Technologies") || android.os.Build.MANUFACTURER.contains("Motorola Solutions")) {
                    scanBarcode();
                    singleScannedInvoice=batchNo;
                }
            }
        });
    }

}