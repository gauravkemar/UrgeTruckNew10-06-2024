package com.android.urgetruck.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.DataModelTrackVehicle;
import com.android.urgetruck.UI.Models.GetExitClearanceModel;
import com.android.urgetruck.UI.Models.JobMilestone;
import com.android.urgetruck.UI.Models.PostRfidResultModel;
import com.android.urgetruck.UI.Models.TrackVehicleModel;
import com.android.urgetruck.UI.Models.TrackVehicleResultModel;
import com.android.urgetruck.UI.Models.WeightDetailsResultModel;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackVehicleFragment extends Fragment {

    private RecyclerView rvTrackVehicle;
    private List<DataModelTrackVehicle> DataModelTrackVehicle;
    private TextView tvvrn,tvDriverName,tvtxn,tvtransaction;
    private ProgressBar progressbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_vehicle,container,false);

        rvTrackVehicle = view.findViewById(R.id.rvTrackVehicle);
        tvvrn = view.findViewById(R.id.tvvrn);
        tvDriverName = view.findViewById(R.id.tvDriverName);
        tvtxn = view.findViewById(R.id.tvtxn);
        tvtransaction = view.findViewById(R.id.tvtransaction);
        progressbar = view.findViewById(R.id.progressbar);
        Bundle bundle = getArguments();


        rvTrackVehicle.setHasFixedSize(true);
        rvTrackVehicle.setLayoutManager(new LinearLayoutManager(getActivity()));


        getVehicleTrackingDetails(bundle.getString("type"),bundle.getString("typevalue"));
        return view;
    }

    private void getVehicleTrackingDetails(String type, String typeValue) {
            if(Utils.isConnected(getActivity())){
                progressbar.setVisibility(View.VISIBLE);
                String baseurl= Utils.getSharedPreferences(getActivity(),"apiurl");
                String port = Utils.getSharedPreferences(getActivity(), "port");
                ApiInterface apiService = APiClient.getClient(baseurl+":"+port).create(ApiInterface.class);
                TrackVehicleModel model = null;
                if(type.equals("RFID")){
                     model = new TrackVehicleModel("12345566",typeValue,"");

                }else if(type.equals("VRN")){
                    model = new TrackVehicleModel("12345566","",typeValue);


                }
                Call<TrackVehicleResultModel> call = apiService.getTrackVehicleDetails(model);
                call.enqueue(new Callback<TrackVehicleResultModel>() {
                    @Override
                    public void onResponse(Call<TrackVehicleResultModel> call, Response<TrackVehicleResultModel> response) {
                        progressbar.setVisibility(View.GONE);
                        if(response.isSuccessful()){


                            tvvrn.append(response.body().getVehicleTransactionDetails().getVrn());
                            tvDriverName.append(response.body().getVehicleTransactionDetails().getDriverName());
                            tvtxn.append(response.body().getVehicleTransactionDetails().getVehicleTransactionCode());
                            if(response.body().getVehicleTransactionDetails().getTranType() == 1){
                                tvtransaction.append("Outbound");
                            }else if(response.body().getVehicleTransactionDetails().getTranType() == 2){
                                tvtransaction.append("Inbound");

                            }else if(response.body().getVehicleTransactionDetails().getTranType() == 3){
                                tvtransaction.append("Internal");

                            }
                            List<JobMilestone> milestones = response.body().getVehicleTransactionDetails().getJobMilestones();
                            //Collections.reverse(milestones);
                            TrackVehicleRecyclerView adapter = new TrackVehicleRecyclerView(getActivity(),milestones);
                            rvTrackVehicle.setAdapter(adapter);



                        }else{
                            PostRfidResultModel message = new Gson().fromJson(response.errorBody().charStream(), PostRfidResultModel.class);
                            Log.e("msg",message.getStatusMessage());
                            Utils.showCustomDialogFinish(getActivity(),message.getStatusMessage());


                        }

                        // progressBar.setVisibility(View.GONE);




                    }

                    @Override
                    public void onFailure(Call<TrackVehicleResultModel> call, Throwable t) {
                        Log.d("TAG","Response = "+t.toString());
                        progressbar.setVisibility(View.GONE);
                        Utils.showCustomDialog(getActivity(),t.toString());

                    }
                });

            }else{
                Utils.showCustomDialogFinish(getActivity(),getString(R.string.internet_connection));
            }

        }
    }


