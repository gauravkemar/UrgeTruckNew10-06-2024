package com.android.urgetruck.UI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.PostRfidResultModel;
import com.android.urgetruck.UI.Models.SecurityCheckModel;
import com.android.urgetruck.UI.Models.SecurityCheckResultModel;
import com.android.urgetruck.UI.Models.WBListResultModel;
import com.android.urgetruck.UI.Models.WBResponseModel;
import com.android.urgetruck.UI.Models.WeightDetailsResultModel;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.FileUtil;
import com.android.urgetruck.UI.Utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SecurityInspectionFragment extends Fragment {

    @Nullable
    private ProgressBar progressBar;
    private TextView tvOriginalWeight, tvNewWeight;
    private RadioGroup radioGroup, radioGroupWbSelection;
    RadioButton rbAuto, rbManual;
    CheckBox cbWbAllocate;
    private WeightDetailsResultModel weightDetailsResultModel;
    private List<WBListResultModel> WBListResultModel;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private TextInputLayout textInputLayoutReason, textInputLayoutWbSelection;
    private ImageView selectedImage;
    private List<Uri> files = new ArrayList<>();
    private LinearLayout parentLinearLayout, layoutWbSelection;
    private Bundle bundle;
    private String reason = "Accept";
    private TextView tvVrn;
    private int weightBridgeId;
    private AutoCompleteTextView actvWb;
    private boolean auto = true;
    ArrayList<Integer> wbId;
    ArrayList wbName;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_security, container, false);
        progressBar = view.findViewById(R.id.progressbar);
        tvOriginalWeight = view.findViewById(R.id.tvOriginalWeight);
        tvNewWeight = view.findViewById(R.id.tvNewWeight);
        radioGroup = view.findViewById(R.id.radioGroup);
//        rbAuto = view.findViewById(R.id.rbAuto);
//        rbManual = view.findViewById(R.id.rbManual);
        textInputLayoutReason = view.findViewById(R.id.textInputLayoutReason);
        cbWbAllocate = view.findViewById(R.id.cbWbAllocate);
        tvVrn = view.findViewById(R.id.tvVrn);
        layoutWbSelection = view.findViewById(R.id.layoutWbSelection);
        //radioGroupWbSelection = view.findViewById(R.id.radioGroupWbSelection);
        textInputLayoutWbSelection = view.findViewById(R.id.textInputLayoutWbSelection);
        actvWb = view.findViewById(R.id.actvWbSelection);
        wbId = new ArrayList();
        wbName = new ArrayList();

        bundle = getArguments();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbAccept) {
                    reason = "Accept";
                    auto = true;
                    actvWb.setText("");
                    cbWbAllocate.setChecked(true);
                    layoutWbSelection.setVisibility(View.GONE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbReprocess) {
                    reason = "AcceptWithReweighment";
                    layoutWbSelection.setVisibility(View.VISIBLE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbReject) {
                    reason = "Reject";
                    auto = true;
                    actvWb.setText("");
                    cbWbAllocate.setChecked(true);
                    layoutWbSelection.setVisibility(View.GONE);
                }
                Log.e("reason", reason);
            }
        });


        parentLinearLayout = view.findViewById(R.id.parent_linear_layout);

        ImageView addImage = view.findViewById(R.id.iv_add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        populateDropdown(view);
        getWeighmentDetails(bundle.getString("type"), bundle.getString("typevalue"));

        view.findViewById(R.id.btnPostSecurityCheck).setOnClickListener(view1 -> {
            confirmInput(view1);

        });
        getAllWeighBridge();

        /*radioGroupWbSelection.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbAuto) {
                auto = true;
                textInputLayoutWbSelection.setVisibility(View.INVISIBLE);
                rbManual.setVisibility(View.VISIBLE);
                actvWb.setText("");
                radioGroup.check(R.id.rbAuto);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbManual) {
                auto = false;
                textInputLayoutWbSelection.setVisibility(View.VISIBLE);
                rbManual.setVisibility(View.INVISIBLE);
                radioGroup.check(R.id.rbManual);
            }
        });*/
        cbWbAllocate.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                textInputLayoutWbSelection.setVisibility(View.INVISIBLE);
                actvWb.setText("");
                auto = true;
            } else {
                textInputLayoutWbSelection.setVisibility(View.VISIBLE);
                auto = false;
            }
        });

        return view;


    }


    private void getWeighmentDetails(String type, String typeValue) {
        if (Utils.isConnected(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            String baseurl = Utils.getSharedPreferences(getActivity(), "apiurl");
            ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
            Call<WeightDetailsResultModel> call = null;
            if (type.equals("RFID")) {
                call = apiService.getWeightDetails(123456789, typeValue, "");


            } else if (type.equals("VRN")) {
                call = apiService.getWeightDetails(123456789, "", typeValue);

            }
            call.enqueue(new Callback<WeightDetailsResultModel>() {
                @Override
                public void onResponse(Call<WeightDetailsResultModel> call, Response<WeightDetailsResultModel> response) {

                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        weightDetailsResultModel = response.body();
                        tvVrn.setText(weightDetailsResultModel.getWeighmentDetails().getVrn());
                        tvOriginalWeight.setText(weightDetailsResultModel.getWeighmentDetails().getExpectedWeight());
                        tvNewWeight.setText(weightDetailsResultModel.getWeighmentDetails().getActualWeight());

                    } else {
                        PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                        Log.e("msg", message.getStatusMessage());
                        Utils.showCustomDialogFinish(getActivity(), message.getStatusMessage());


                    }

                }

                @Override
                public void onFailure(Call<WeightDetailsResultModel> call, Throwable t) {
                    Log.d("TAG", "Response = " + t.toString());
                    progressBar.setVisibility(View.GONE);
                   // Utils.showCustomDialog(getActivity(), t.toString());
                    if (t instanceof SocketTimeoutException) {
                        // Handle timeout exception with custom message
                        Utils.showCustomDialog(getActivity(),"Network error,\n Please check Network!!");
                    } else {
                        // Handle other exceptions
                        Utils.showCustomDialog(getActivity(), t.toString());

                    }
                }
            });

        } else {
            Utils.showCustomDialogFinish(getActivity(), getString(R.string.internet_connection));
        }

    }

    private void getAllWeighBridge() {
        if (Utils.isConnected(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            String baseurl = Utils.getSharedPreferences(getActivity(), "apiurl");
            ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
            Call<WBResponseModel> call = null;

            call = apiService.getAllWeighBridgeList();

            call.enqueue(new Callback<WBResponseModel>() {
                @Override
                public void onResponse(Call<WBResponseModel> call, Response<WBResponseModel> response) {

                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        WBResponseModel wbResponseModel = response.body();
                        List<WBListResultModel> jsonArray = wbResponseModel.getWbListResultModels();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            wbId.add(jsonArray.get(i).getWbId());
                            wbName.add(jsonArray.get(i).getWbName());
                        }
                        populateWbDropdown(wbName);

                    } else {
                        PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                        Log.e("msg", message.getStatusMessage());
                        Utils.showCustomDialogFinish(getActivity(), message.getStatusMessage());


                    }

                }

                @Override
                public void onFailure(Call<WBResponseModel> call, Throwable t) {
                    Log.d("TAG", "Response = " + t.toString());
                    progressBar.setVisibility(View.GONE);
                    //Utils.showCustomDialog(getActivity(), t.toString());
                    if (t instanceof SocketTimeoutException) {
                        // Handle timeout exception with custom message
                        Utils.showCustomDialog(getActivity(),"Network error,\n Please check Network!!");
                    } else {
                        // Handle other exceptions
                        Utils.showCustomDialog(getActivity(),t.toString());
                    }
                }
            });

        } else {
            Utils.showCustomDialogFinish(getActivity(), getString(R.string.internet_connection));
        }

    }

    public void populateDropdown(View view) {


        String[] reason = new String[]{"WB Not working", "Vehicle Defect", "Other"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.dropdown_menu_popup_item,
                        reason);

        AutoCompleteTextView editTextFilledExposedDropdown1 =
                view.findViewById(R.id.autoCompleteTextViewrsecurityreason);
        editTextFilledExposedDropdown1.setAdapter(adapter);
    }

    public void populateWbDropdown(ArrayList wbName) {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.dropdown_menu_popup_item,
                        wbName);

        actvWb.setAdapter(adapter);
    }

    private boolean validateWbDropdown() {
        String wbSpinner = textInputLayoutWbSelection.getEditText().getText().toString().trim();

        if (wbSpinner.isEmpty()) {
            textInputLayoutWbSelection.setError("Please Select a WeighBridge");
            return false;
        } else {
            textInputLayoutWbSelection.setError(null);
            return true;
        }
    }

    private boolean validateReason() {
        String reasonInput = textInputLayoutReason.getEditText().getText().toString().trim();

        if (reasonInput.isEmpty()) {
            textInputLayoutReason.setError("Please Select a reason");
            return false;
        } else {
            textInputLayoutReason.setError(null);
            return true;
        }
    }

    private boolean validateWb() {
        String wbInput = textInputLayoutWbSelection.getEditText().getText().toString().trim();

        if (wbInput.isEmpty()) {
            textInputLayoutWbSelection.setError("Please Select a WeighBridge");
            return false;
        } else {
            textInputLayoutWbSelection.setError(null);
            return true;
        }
    }


    public void confirmInput(View v) {
        if (!auto) {
            if (!validateWb()) {
                return;
            }
        }

        if (!validateReason()) {
            return;
        }
        if (!Utils.isConnected(getActivity())) {
            Utils.showCustomDialog(getActivity(), getString(R.string.internet_connection));
        } else {

            // callPostSecurityCheck(v);

            requestPermission();
            uploadImages();


        }


        //Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadImages();
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void addImage() {
        if (files.size() > 4) {
            Toast.makeText(getActivity(), "Maximum 5 photos can be uploaded", Toast.LENGTH_SHORT).show();

        } else {
            selectImage(getActivity());

        }

    }

    //===== select image
    private void selectImage(Context context) {
        final CharSequence[] options;
        if (android.os.Build.MANUFACTURER.contains("Zebra Technologies") || android.os.Build.MANUFACTURER.contains("Motorola Solutions")) {
            options = new CharSequence[]{"Choose from Gallery", "Cancel"};

        } else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Choose a Media");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rowView = inflater.inflate(R.layout.image, null);
                        // Add the new row before the add field button.
                        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                        parentLinearLayout.isFocusable();
                        selectedImage = rowView.findViewById(R.id.number_edit_text);
                        Bitmap img = (Bitmap) data.getExtras().get("data");
                        selectedImage.setImageBitmap(img);
                        Picasso.get().load(getImageUri(getActivity(), img)).into(selectedImage);
                        String imgPath = FileUtil.getPath(getActivity(), getImageUri(getActivity(), img));
                        files.add(Uri.parse(imgPath));
                        Log.e("image", imgPath);
                    }

                    break;
                case 1:
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rowView = inflater.inflate(R.layout.image, null);
                        // Add the new row before the add field button.
                        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                        parentLinearLayout.isFocusable();
                        selectedImage = rowView.findViewById(R.id.number_edit_text);
                        Uri img = data.getData();
                        Picasso.get().load(img).into(selectedImage);
                        String imgPath = FileUtil.getPath(getActivity(), img);
                        files.add(Uri.parse(imgPath));
                        Log.e("image", imgPath);

                    }
                    break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "intuenty", null);
        Log.d("image uri", path);
        return Uri.parse(path);
    }

    //===== Upload files to server
    public void uploadImages() {

        progressBar.setVisibility(View.VISIBLE);
        List<MultipartBody.Part> list = new ArrayList<>();

        for (Uri uri : files) {

            Log.i("uris", uri.getPath());

            list.add(prepareFilePart("file", uri));
        }

        String baseurl = Utils.getSharedPreferences(getActivity(), "apiurl");
        ApiInterface serviceInterface = APiClient.getClient(baseurl).create(ApiInterface.class);
        SecurityCheckModel modal = null;

        /*if (bundle.getString("type").equals("RFID")) {
            if (auto) {
                modal = new SecurityCheckModel("123456789", bundle.getString("typevalue"), weightDetailsResultModel.getWeighmentDetails().getJobMilestoneId().toString(), weightDetailsResultModel.getWeighmentDetails().getVehicleTransactionId().toString(), "", "WEIGHMENT DISTURBANCY", reason, 0);
            } else {
                int weighbridgeId = wbId.get(wbName.indexOf(actvWb.getText().toString().trim()));
                modal = new SecurityCheckModel("123456789", bundle.getString("typevalue"), weightDetailsResultModel.getWeighmentDetails().getJobMilestoneId().toString(), weightDetailsResultModel.getWeighmentDetails().getVehicleTransactionId().toString(), "", "WEIGHMENT DISTURBANCY", reason, weighbridgeId);
            }


        } else if (bundle.getString("type").equals("VRN")) {
            if (auto) {
                modal = new SecurityCheckModel("123456789", "", weightDetailsResultModel.getWeighmentDetails().getJobMilestoneId().toString(), weightDetailsResultModel.getWeighmentDetails().getVehicleTransactionId().toString(), bundle.getString("typevalue"), "WEIGHMENT DISTURBANCY", reason, 0);
            } else {
                int weighbridgeId = wbId.get(wbName.indexOf(actvWb.getText().toString().trim()));
                modal = new SecurityCheckModel("123456789", "", weightDetailsResultModel.getWeighmentDetails().getJobMilestoneId().toString(), weightDetailsResultModel.getWeighmentDetails().getVehicleTransactionId().toString(), bundle.getString("typevalue"), "WEIGHMENT DISTURBANCY", reason, weighbridgeId);
            }


        }*/
        if (auto) {
            modal = new SecurityCheckModel("123456789", "", weightDetailsResultModel.getWeighmentDetails().getJobMilestoneId().toString(), weightDetailsResultModel.getWeighmentDetails().getVehicleTransactionId().toString(), weightDetailsResultModel.getWeighmentDetails().getVrn(), "WEIGHMENT DISTURBANCY", reason, 0);
        } else {
            int weighbridgeId = wbId.get(wbName.indexOf(actvWb.getText().toString().trim()));
            modal = new SecurityCheckModel("123456789", "", weightDetailsResultModel.getWeighmentDetails().getJobMilestoneId().toString(), weightDetailsResultModel.getWeighmentDetails().getVehicleTransactionId().toString(), weightDetailsResultModel.getWeighmentDetails().getVrn(), "WEIGHMENT DISTURBANCY", reason, weighbridgeId);
        }

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(modal));

        Call<SecurityCheckResultModel> call = serviceInterface.postSecurityCheck(list, filename);

        call.enqueue(new Callback<SecurityCheckResultModel>() {
            @Override
            public void onResponse(Call<SecurityCheckResultModel> call, Response<SecurityCheckResultModel> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("url", response.raw().request().body().toString());
                Log.d("url", response.raw().request().headers().toString());
                Log.d("url", response.raw().request().url().toString());
                try {

                    if (response.isSuccessful()) {
                        //Toast.makeText(getActivity(), "Files uploaded successfuly", Toast.LENGTH_SHORT).show();
                        Utils.showCustomDialogFinish(getActivity(), response.body().getStatus());
                        Log.e("main", "the status is ----> " + response.body().getStatus());
                        Log.e("main", "the message is ----> " + response.body().getStatusMassage());
                    } else {
                        Log.e("error", new Gson().toJson(response.errorBody()));
                        Utils.showCustomDialog(getActivity(), new Gson().toJson(response.errorBody()));
                    }

                    Log.e("res code", response.code() + "");


                } catch (Exception e) {
                    Log.d("Exception", "|=>" + e.getMessage());
                    Utils.showCustomDialog(getActivity(), e.getMessage());
//
                }
            }

            @Override
            public void onFailure(Call<SecurityCheckResultModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("my", t.getMessage());
               // Utils.showCustomDialog(getActivity(), t.getMessage());

                if (t instanceof SocketTimeoutException) {
                    // Handle timeout exception with custom message
                    Utils.showCustomDialog(getActivity(),"Network error,\n Please check Network!!");
                } else {
                    // Handle other exceptions
                    Utils.showCustomDialog(getActivity(),t.toString());
                }

            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());
        Log.i("here is error", file.getAbsolutePath());
        // create RequestBody instance from file

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}