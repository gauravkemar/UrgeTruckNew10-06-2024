package com.android.urgetruck.UI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.ExistCheck;
import com.android.urgetruck.UI.Models.ExitClearanceParameters;
import com.android.urgetruck.UI.Models.ExitClearanceResultModel;
import com.android.urgetruck.UI.Models.GetCheckListItemsModel;
import com.android.urgetruck.UI.Models.GetExitClearanceModel;
import com.android.urgetruck.UI.Models.PostExitClearanceModel;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.FileUtil;
import com.android.urgetruck.UI.Utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExitClearanceFragment extends Fragment {

    @Nullable
    ProgressBar progressBar;
    private ListView lvCheckboxes;
    public static TextView uploadphoto;
    private GetExitClearanceModel getExitClearanceModel;
    private TextView tvVrn, tvDriverName;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private ImageView selectedImage;
    List<Uri> files = new ArrayList<>();
    private LinearLayout parentLinearLayout;
    private List<ExistCheck> existCheck;
    private LinearLayout linearLayout;
    private ArrayList<ExitClearanceParameters> exitClearanceParameters;
    private HashMap<Integer, ExitClearanceParameters> parametersHashMap = new HashMap<>();
    private HashMap<Integer, Boolean> mandatoryItems;
    private String mandatoryItemsValue = "";


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_physical_check, container, false);

        Bundle bundle = getArguments();
        getExitClearanceModel = (GetExitClearanceModel) bundle.getSerializable("data");
        Log.e("result", getExitClearanceModel.getExitClearanceDetails().getDriverName());
        tvVrn = view.findViewById(R.id.tvVrn);
        tvDriverName = view.findViewById(R.id.tvDriverName);
        progressBar = view.findViewById(R.id.progressbar);
        linearLayout = view.findViewById(R.id.linearLayout);
        tvVrn.setText(getExitClearanceModel.getExitClearanceDetails().getVrn());
        tvDriverName.setText(getExitClearanceModel.getExitClearanceDetails().getDriverName());
        exitClearanceParameters = new ArrayList<>();
        getCheckListItems();


        parentLinearLayout = view.findViewById(R.id.parent_linear_layout);

        ImageView addImage = view.findViewById(R.id.iv_add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        view.findViewById(R.id.btSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput(view);
            }
        });


        return view;
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

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
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
//        SecurityCheckModel modal = null;
//
//        if (bundle.getString("type").equals("RFID")) {
//            modal = new SecurityCheckModel("123456789", bundle.getString("RFID"), weightDetailsResultModel.getWeighmentDetails().getJobMilestoneId().toString(), weightDetailsResultModel.getWeighmentDetails().getVehicleTransactionId().toString(), "", "WEIGHMENT DISTURBANCY", reason);
//
//
//        } else if (bundle.getString("type").equals("VRN")) {
//            modal = new SecurityCheckModel("123456789", "", weightDetailsResultModel.getWeighmentDetails().getJobMilestoneId().toString(), weightDetailsResultModel.getWeighmentDetails().getVehicleTransactionId().toString(), bundle.getString("VRN"), "WEIGHMENT DISTURBANCY", reason);
//
//        }
        // ArrayList<ExitClearanceParameters> exitClearanceParameters = new ArrayList<>();
        exitClearanceParameters.add(new ExitClearanceParameters("Vehicle Body Appearance", "OK", "", ""));
        ArrayList<ExitClearanceParameters> parameters = new ArrayList<>(parametersHashMap.values());


        PostExitClearanceModel modal = new PostExitClearanceModel("12346678", getExitClearanceModel.getExitClearanceDetails().getVrn(), getExitClearanceModel.getExitClearanceDetails().getVehicleTransactionId().toString(), getExitClearanceModel.getExitClearanceDetails().getJobMilestoneId().toString(), parameters);
        Log.e("request", new Gson().toJson(modal));


        String json = new Gson().toJson(modal);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(modal));

        Call<ExitClearanceResultModel> call = serviceInterface.postExitClearance(list, filename);
        call.enqueue(new Callback<ExitClearanceResultModel>() {
            @Override
            public void onResponse(Call<ExitClearanceResultModel> call, Response<ExitClearanceResultModel> response) {
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
            public void onFailure(Call<ExitClearanceResultModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("my", t.getMessage());
                Utils.showCustomDialog(getActivity(), t.getMessage());

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

    public void confirmInput(View v) {
//        if (!validateReason()) {
//            return;
//        }
        ArrayList<Boolean> bollist = new ArrayList<Boolean>(mandatoryItems.values());

        if (!Utils.isConnected(getActivity())) {
            Utils.showCustomDialog(getActivity(), getString(R.string.internet_connection));
        } else if (!areAllTrue(bollist)) {
            Utils.showCustomDialog(getActivity(), "PLEASE SELECT ALL MANDATORY FIELDS :\n" + mandatoryItemsValue);

        } else {
            // Utils.showCustomDialog(getActivity(),"done");

            ArrayList<ExitClearanceParameters> parameters = new ArrayList<>(parametersHashMap.values());


            Log.e("request", new Gson().toJson(parameters));

            // callPostSecurityCheck(v);

            requestPermission();
            uploadImages();


        }

    }

    private void getCheckListItems() {
        if (Utils.isConnected(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            String baseurl = Utils.getSharedPreferences(getActivity(), "apiurl");
            ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);
            Call<GetCheckListItemsModel> call = apiService.getCheckListItems(123456789);
            call.enqueue(new Callback<GetCheckListItemsModel>() {
                @Override
                public void onResponse(Call<GetCheckListItemsModel> call, Response<GetCheckListItemsModel> response) {

                    progressBar.setVisibility(View.GONE);

                    existCheck = response.body().getExistCheckList();
                    mandatoryItems = new HashMap<>();

                    ArrayList<String> CheckListDataArray = new ArrayList<>();
                    for (int i = 0; i < existCheck.size(); i++) {
                        if (existCheck.get(i).getIsMandatory()) {
                            mandatoryItems.put(i, false);
                            mandatoryItemsValue = mandatoryItemsValue + existCheck.get(i).getChecklistItem() + ".\n";
                        }
                        CheckListDataArray.add(existCheck.get(i).getChecklistItem());
                        CheckBox checkBox = new CheckBox(getActivity());
                        if (existCheck.get(i).getIsMandatory()) {
                            checkBox.setText(existCheck.get(i).getChecklistItem() + " **");
                        } else {
                            checkBox.setText(existCheck.get(i).getChecklistItem());
                        }
                        checkBox.setOnClickListener(OnCheckListClick(checkBox, i));
                        linearLayout.addView(checkBox);

                    }
                    for (ExistCheck check : existCheck) {


                    }


                    Log.e("response", response.body().toString());


                }

                @Override
                public void onFailure(Call<GetCheckListItemsModel> call, Throwable t) {
                    Log.d("TAG", "Response = " + t.toString());
                    progressBar.setVisibility(View.GONE);
                    Utils.showCustomDialogFinish(getActivity(), t.toString());

                }
            });

        } else {
            Utils.showCustomDialogFinish(getActivity(), getString(R.string.internet_connection));
        }

    }

    private View.OnClickListener OnCheckListClick(CheckBox checkBox, int pos) {
        return new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    //   exitClearanceParameters.add(new ExitClearanceParameters(checkBox.getText().toString(), "OK", "", ""));
                    mandatoryItems.replace(pos, true);
                    parametersHashMap.put(pos, new ExitClearanceParameters(checkBox.getText().toString(), "OK", "", ""));
                } else if (!checkBox.isChecked()) {
                    if (!parametersHashMap.isEmpty()) {
                        parametersHashMap.remove(pos);
                        mandatoryItems.replace(pos, false);

                    }

                }

                Log.e("params", "" + parametersHashMap.size());


            }
        };
    }

    public boolean areAllTrue(ArrayList<Boolean> array) {

        for (boolean b : array) {
            if (!b) return false;
        }
        return true;
    }
}
