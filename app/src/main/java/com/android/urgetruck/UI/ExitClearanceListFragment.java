package com.android.urgetruck.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.GetExitClearanceModel;
import com.android.urgetruck.UI.Models.PostRfidResultModel;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.Utils;
import com.google.gson.Gson;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.Scanner.TriggerType;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;
import com.symbol.emdk.barcode.StatusData.ScannerStates;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExitClearanceListFragment extends Fragment implements EMDKManager.EMDKListener, Scanner.StatusListener, Scanner.DataListener {
    @Nullable

    public static Button btnproceed;
    public static RecyclerView rvExitClearanceList;

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;
    private ExitClearanceRecyclerViewAdapter adapter;
    private ArrayList<Boolean> checkList;
    private ProgressBar progressbar;
    private int pos;
    GetExitClearanceModel getExitClearanceModel;
    private TextView tvVrn, tvDriverName;
    private Boolean check = false;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_physical_checklist, container, false);

        btnproceed = view.findViewById(R.id.btnproceed);
        progressbar = view.findViewById(R.id.progressbar);
        tvVrn = view.findViewById(R.id.tvVrn);
        tvDriverName = view.findViewById(R.id.tvDriverName);
        Bundle bundle = getArguments();

        if (android.os.Build.MANUFACTURER.contains("Zebra Technologies") || android.os.Build.MANUFACTURER.contains("Motorola Solutions")) {
            EMDKResults results = EMDKManager.getEMDKManager(getActivity(), this);

// Check the return status of getEMDKManager() and update the status TextView accordingly.
            if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
                updateStatus("EMDKManager object request failed!");

            } else {
                updateStatus("EMDKManager object initialization is   in   progress.......");
            }
        }


        getDeviceName();
        rvExitClearanceList = view.findViewById(R.id.rvExitClearanceList);
        rvExitClearanceList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvExitClearanceList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        rvExitClearanceList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rvExitClearanceList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever

                        if (!checkList.get(position)) {
                            //pos = position;
                            //initBarcodeManager();
                            //initScanner();


                            if (android.os.Build.MANUFACTURER.contains("Zebra Technologies") || android.os.Build.MANUFACTURER.contains("Motorola Solutions")) {
                                scanBarcode();
                            } else {
                                //Utils.showCustomDialogFinish(getActivity(), "Functionality not Available on non Zebra Devices");
                                scanBarcodeFromCamera();
                            }


                            //Utils.showCustomDialog(getActivity(),"Press Trigger to Scan Barcode");

                        } else {
                            //   Toast.makeText(getActivity(),"Barcode already verified",Toast.LENGTH_SHORT).show();
                            Utils.showCustomDialog(getActivity(), "Barcode already verified");
                        }


                    }


                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        getClearanceList(bundle.getString("type"), bundle.

                getString("typevalue"));

        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (areAllTrue(checkList)) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("data", getExitClearanceModel);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    ExitClearanceFragment exitClearanceFragment = new ExitClearanceFragment();
                    exitClearanceFragment.setArguments(bundle1);
                    transaction.replace(R.id.physicalcheckfragment_container, exitClearanceFragment);
                    transaction.commit();
                } else {
                    Utils.showCustomDialog(getActivity(), "Please Verify all barcodes");

                }

//                Bundle bundle1 = new Bundle();
//                bundle1.putSerializable("data",getExitClearanceModel);
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                ExitClearanceFragment exitClearanceFragment = new ExitClearanceFragment();
//                exitClearanceFragment.setArguments(bundle1);
//                transaction.replace(R.id.physicalcheckfragment_container, exitClearanceFragment);
//                transaction.commit();

            }
        });

        return view;
    }

    public void getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        Log.e("result", manufacturer + "   " + model);

    }

    private void getClearanceList(String type, String typeValue) {
        if (Utils.isConnected(getActivity())) {
            progressbar.setVisibility(View.VISIBLE);
            String baseurl = Utils.getSharedPreferences(getActivity(), "apiurl");
            String port = Utils.getSharedPreferences(getActivity(), "port");
            ApiInterface apiService = APiClient.getClient(baseurl+":"+port).create(ApiInterface.class);
            Call<GetExitClearanceModel> call = null;
            if (type.equals("RFID")) {
                call = apiService.getExitClearanceDetails(123456789, typeValue, "");


            } else if (type.equals("VRN")) {
                call = apiService.getExitClearanceDetails(123456789, "", typeValue);

            }
            call.enqueue(new Callback<GetExitClearanceModel>() {
                @Override
                public void onResponse(Call<GetExitClearanceModel> call, Response<GetExitClearanceModel> response) {
                    progressbar.setVisibility(View.GONE);
                    Log.d("url", response.raw().request().headers().toString());
                    Log.d("url", response.raw().request().url().toString());
                    if (response.isSuccessful()) {
                        try {
                            Log.e("result", response.body().getExitClearanceDetails().getProducts().get(0).getProductName());
                            getExitClearanceModel = response.body();
                            tvVrn.setText(getExitClearanceModel.getExitClearanceDetails().getVrn());
                            tvDriverName.setText(getExitClearanceModel.getExitClearanceDetails().getDriverName());
                            checkList = new ArrayList<>(getExitClearanceModel.getExitClearanceDetails().getProducts().size());
                            for (int i = 0; i < response.body().getExitClearanceDetails().getProducts().size(); i++) {
                                checkList.add(false);
                            }
                            adapter = new ExitClearanceRecyclerViewAdapter(getActivity(), getExitClearanceModel.getExitClearanceDetails().getProducts(), checkList);
                            rvExitClearanceList.setAdapter(adapter);
                        }
                        catch (Exception e) {

                        }

                    } else {
                        try {
                            PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                            Log.e("msg", message.getStatusMessage());
                            Utils.showCustomDialogFinish(getActivity(), message.getStatusMessage());
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(), "Exit Clearance Error!!", Toast.LENGTH_SHORT).show();
                        }
                        


                    }

                    // progressBar.setVisibility(View.GONE);


                }

                @Override
                public void onFailure(Call<GetExitClearanceModel> call, Throwable t) {
                    Log.d("TAG", "Response = " + t.toString());
                    progressbar.setVisibility(View.GONE);
                    Utils.showCustomDialog(getActivity(), t.toString());

                }
            });

        } else {
            Utils.showCustomDialogFinish(getActivity(), getString(R.string.internet_connection));
        }
    }


    @Override
    public void onOpened(EMDKManager emdkManager) {

        if (android.os.Build.MANUFACTURER.contains("Zebra Technologies") || android.os.Build.MANUFACTURER.contains("Motorola Solutions")) {
            this.emdkManager = emdkManager;

            initBarcodeManager();

            initScanner();
        }

    }

    public static void check() {

    }

    @Override
    public void onClosed() {
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
        updateStatus("EMDK closed unexpectedly! Please close and restart the application.");


    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        String dataStr = "";
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanData> scanData = scanDataCollection.getScanData();
            // Iterate through scanned data and prepare the data.
            for (ScanData data : scanData) {
                // Get the scanned data
                String barcodeData = data.getData();
                // Get the type of label being scanned
                ScanDataCollection.LabelType labelType = data.getLabelType();
                // Concatenate barcode data and label type
                dataStr = barcodeData;
            }
            // Update EditText with scanned data and type of label on UI thread.
            updateData(dataStr);
        }

    }

    @Override
    public void onStatus(StatusData statusData) {
        // The status will be returned on multiple cases. Check the state and take the action.
// Get the current state of scanner in background
        ScannerStates state = statusData.getState();
        String statusStr = "";
// Different states of Scannerswitch (state) {
        switch (state) {
            case IDLE:
                // Scanner is idle and ready to change configuration and submit read.
                statusStr = statusData.getFriendlyName() + " is   enabled and idle...";
                // Change scanner configuration. This should be done while the scanner is in IDLE state.
                setConfig();
                try {
                    // Starts an asynchronous Scan. The method will NOT turn ON the scanner beam,
                    //but puts it in a  state in which the scanner can be turned on automatically or by pressing a hardware trigger.
                    scanner.read();
                } catch (ScannerException e) {
                    updateStatus(e.getMessage());
                }
                break;
            case WAITING:
                // Scanner is waiting for trigger press to scan...
                statusStr = "Scanner is waiting for trigger press...";
                break;
            case SCANNING:
                // Scanning is in progress...
                statusStr = "Scanning...";
                break;
            case DISABLED:
                // Scanner is disabledstatusStr = statusData.getFriendlyName()+" is disabled.";
                break;
            case ERROR:
                // Error has occurred during scanning
                statusStr = "An error has occurred.";
                break;
            default:
                break;
        }
        // Updates TextView with scanner state on UI thread.
        updateStatus(statusStr);

    }

    private void initBarcodeManager() {
        // Get the feature object such as BarcodeManager object for accessing the feature.
        barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
        // Add external scanner connection listener.
        if (barcodeManager == null) {
            Toast.makeText(getActivity(), "Barcode scanning is not supported.", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
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
                scanner.triggerType = TriggerType.SOFT_ONCE;

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

    private void initScanner() {
//        if (scanner == null  || scanner != null) {
//            // Get default scanner defined on the device
//            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
//            if(scanner != null) {
//                // Implement the DataListener interface and pass the pointer of this object to get the data callbacks.
//                scanner.addDataListener(this);
//
//                // Implement the StatusListener interface and pass the pointer of this object to get the status callbacks.
//                scanner.addStatusListener(this);
////                ScannerConfig config = null;
////                try {
////                    config = scanner.getConfig();
////                    config.readerParams.readerSpecific.imagerSpecific.scanMode = ScannerConfig.ScanMode.MULTI_BARCODE;
////                    // If it is a camera
////                    config.readerParams.readerSpecific.cameraSpecific.scanMode = ScannerConfig.ScanMode.MULTI_BARCODE;
////
////                    // Setting the barcode count
////
////
////                    scanner.setConfig(config);
////                } catch (ScannerException e) {
////                    e.printStackTrace();
////                }
//
//                // Scan Mode set to Multi Barcode
//                // If it is a imager
//
//
//                // Hard trigger. When this mode is set, the user has to manually
//                // press the trigger on the device after issuing the read call.
//                // NOTE: For devices without a hard trigger, use TriggerType.SOFT_ALWAYS.
//                scanner.triggerType =  TriggerType.HARD;
//
//                try{
//                    // Enable the scanner
//                    // NOTE: After calling enable(), wait for IDLE status before calling other scanner APIs
//                    // such as setConfig() or read().
//                    scanner.enable();
//                    scanner.read();
//
//                } catch (ScannerException e) {
//                    updateStatus(e.getMessage());
//                    deInitScanner();
//                }
//            } else {
//                updateStatus("Failed to   initialize the scanner device.");
//            }
//        }

        if (scanner == null) {
            // Get the Barcode Manager object
            barcodeManager = (BarcodeManager) this.emdkManager
                    .getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
            // Get default scanner defined on the device
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
            // Add data and status listeners
            scanner.addDataListener(this);
            scanner.addStatusListener(this);
            // Hard trigger. When this mode is set, the user has to manually
            // press the trigger on the device after issuing the read call.
            scanner.triggerType = TriggerType.HARD;
            // Enable the scanner
            try {
                scanner.enable();
            } catch (ScannerException e) {
                e.printStackTrace();
            }
            //set startRead flag to true. this flag will be used in the OnStatus callback to insure
            //the scanner is at an IDLE state and a read is not pending before calling scanner.read()
            //startRead = true;
        }


    }

    private void deInitScanner() {
        if (scanner != null) {
            try {
                // Release the scanner
                scanner.release();
            } catch (Exception e) {
                updateStatus(e.getMessage());
            }
            scanner = null;
        }
    }

    // Variable to hold scan data length
    private int dataLength = 0;

    public void updateData(String result) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(getActivity(),""+result,Toast.LENGTH_SHORT).show();
                for (int i = 0; i < getExitClearanceModel.getExitClearanceDetails().getProducts().size(); i++) {
                    if (result.equals(getExitClearanceModel.getExitClearanceDetails().getProducts().get(i).getBatchNumber())) {
                        checkList.set(i, true);
                        adapter.notifyItemChanged(i);

                    }
                }


                if (dataLength++ >= 50) {
                    // Clear the cache after 50 scans
                    // dataView.getText().clear();
                    dataLength = 0;
                }


            }
        });


    }

    private void updateStatus(final String status) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // statusTextView.setText(""+  status);


            }
        });

    }

    private void setConfig() {
        if (scanner != null) {
            try {
                // Get scanner config
                ScannerConfig config = scanner.getConfig();
                // Enable haptic feedback
                if (config.isParamSupported("config.scanParams.decodeHapticFeedback")) {
                    config.scanParams.decodeHapticFeedback = true;
                }
                // Set scanner config
                scanner.setConfig(config);
            } catch (ScannerException e) {
                updateStatus(e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    public boolean areAllTrue(ArrayList<Boolean> array) {
        //  Toast.makeText(getActivity(),""+array.size(),Toast.LENGTH_SHORT).show();

        for (boolean b : array) {
            if (!b) return false;
        }
        return true;
    }

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private void scanBarcodeFromCamera() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } else {
            startActivity(new Intent(getActivity(), ScanBarcodeActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), ScanBarcodeActivity.class));
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 101);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!ExitClearanceActivity.result.isEmpty() || !ExitClearanceActivity.result.equals("")) {
            updateData(ExitClearanceActivity.result);
        }
    }
}
